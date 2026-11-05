package org.azotiques.sonantv1.user.services;

import lombok.RequiredArgsConstructor;
import org.azotiques.sonantv1.user.UserDto;
import org.azotiques.sonantv1.user.mappers.UserMapper;
import org.azotiques.sonantv1.user.repositories.UserRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDto getUser(Jwt jwt) {
        return UserMapper.toUserDto(userRepository.findBySub(jwt.getSubject())
                .orElseThrow(() -> new RuntimeException("User not found")));
    }
}