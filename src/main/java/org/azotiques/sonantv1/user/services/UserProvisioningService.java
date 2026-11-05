package org.azotiques.sonantv1.user.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.azotiques.sonantv1.user.entities.User;
import org.azotiques.sonantv1.user.repositories.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProvisioningService {

    private final UserRepository userRepository;

    @Transactional
    public User save(Jwt jwt) {
        String sub = jwt.getSubject();
        return userRepository.findBySub(sub)
                .orElseGet(() -> {
                    String username = jwt.getClaimAsString("preferred_username");

                    if(username == null) {
                        throw new IllegalArgumentException("Username not found");
                    }

                    User user = new User();
                    user.setSub(sub);
                    user.setUsername(username);
                    try {
                        return userRepository.saveAndFlush(user);
                    } catch (DataIntegrityViolationException ex) {
                        return userRepository.findBySub(sub).orElseThrow(() -> new IllegalArgumentException("Username not found"));
                    }
                });
    }

}
