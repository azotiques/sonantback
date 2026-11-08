package org.azotiques.sonantv1.realtime.services;

import lombok.RequiredArgsConstructor;
import org.azotiques.sonantv1.user.UserApi;
import org.azotiques.sonantv1.user.UserDto;
import org.azotiques.sonantv1.user.UserStatus;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class PresenceService {

    private static final Duration TTL = Duration.ofSeconds(60);
    private final StringRedisTemplate redisTemplate;

    private String key(String userId) {
        return "presence:sessions:" + userId;
    }

    public boolean heartbeat(String userId, String sessionId) {
        String k = key(userId);

        Boolean wasOnline = Boolean.TRUE.equals(redisTemplate.hasKey(k));

        redisTemplate.opsForSet().add(k, sessionId);
        redisTemplate.expire(k, TTL);

        return !wasOnline;
    }

    public boolean markOffline (String userId, String sessionId) {
        String k = key(userId);

        Long removed = redisTemplate.opsForSet().remove(k, sessionId);
        if (removed == null || removed == 0) {
            return false;
        }

        Long remainingSessions = redisTemplate.opsForSet().size(k);

        return remainingSessions == null || remainingSessions == 0;
    }

    public String getStatus(String userId) {
        Long size =
                redisTemplate.opsForSet().size(key(userId));

        return (size != null && size > 0) ? "ONLINE" : "OFFLINE";
    }

}
