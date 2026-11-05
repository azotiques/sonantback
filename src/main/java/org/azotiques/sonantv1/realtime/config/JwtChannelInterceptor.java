package org.azotiques.sonantv1.realtime.config;

import lombok.RequiredArgsConstructor;
import org.azotiques.sonantv1.guild.GuildApi;
import org.azotiques.sonantv1.user.UserApi;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JwtDecoder jwtDecoder;
    private final UserApi userApi;
    private final GuildApi guildApi;
    private static final String PATTERN = "/topic/chat.guild.{guildId}.{channelId}";
    private final AntPathMatcher matcher = new AntPathMatcher();


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if(StompCommand.CONNECT.equals(accessor.getCommand())) {
            try {
                Jwt jwt = jwtDecoder.decode(accessor.getFirstNativeHeader("Authorization").substring(7));
                var user = new UsernamePasswordAuthenticationToken(jwt, null, List.of());
                userApi.provision(jwt);
                accessor.setUser(user);
            } catch (JwtException e) {
                e.printStackTrace();
                throw e;
            }
        }

        if(StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            Principal principal = accessor.getUser();

            var auth = (UsernamePasswordAuthenticationToken) principal;

            Jwt jwt = (Jwt) auth.getPrincipal();

            if(matcher.match(PATTERN, accessor.getDestination())) {
                Map<String, String> variables = matcher.extractUriTemplateVariables(PATTERN, accessor.getDestination());
                String guildId = variables.get("guildId");
                String channelId = variables.get("channelId");

                guildApi.guildCheck(jwt, guildId, channelId);
            } else {
                throw new AccessDeniedException("Access denied");
            }
        }

        return message;
    }
}
