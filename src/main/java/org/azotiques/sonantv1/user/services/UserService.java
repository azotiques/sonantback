package org.azotiques.sonantv1.user.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.azotiques.sonantv1.storage.FileStorageApi;
import org.azotiques.sonantv1.user.UserDto;
import org.azotiques.sonantv1.user.UserNotFoundException;
import org.azotiques.sonantv1.user.dtos.EditUserRequest;
import org.azotiques.sonantv1.user.entities.User;
import org.azotiques.sonantv1.user.mappers.UserMapper;
import org.azotiques.sonantv1.user.repositories.UserRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FileStorageApi fileStorage;
    private final UserMapper userMapper;

    public UserDto getUser(Jwt jwt) {
        return userMapper.toUserDto(userRepository.findBySub(jwt.getSubject())
                .orElseThrow(() -> new UserNotFoundException("User not found")));
    }

    @Transactional
    public UserDto editUser(Jwt jwt, EditUserRequest request) {
        User user = userRepository.findBySub(jwt.getSubject()).orElseThrow(() -> new UserNotFoundException("User not found"));

        var avatar = request.avatar();

        if (avatar != null && !avatar.isEmpty()) {
            String contentType = avatar.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new IllegalArgumentException("Avatar must be an image");
            }

            String oldKey = user.getAvatar();
            String newKey = fileStorage.upload(avatar);

            if (oldKey == null) {
                user.setAccountSetup(true);
            }

            user.setAvatar(newKey);
            user = userRepository.save(user);

            if (oldKey != null) {
                fileStorage.delete(oldKey);
            }
        }

        return userMapper.toUserDto(user);
    }
}