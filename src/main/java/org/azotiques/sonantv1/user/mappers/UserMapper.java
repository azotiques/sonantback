package org.azotiques.sonantv1.user.mappers;

import lombok.RequiredArgsConstructor;
import org.azotiques.sonantv1.storage.FileStorageApi;
import org.azotiques.sonantv1.user.UserDto;
import org.azotiques.sonantv1.user.entities.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final FileStorageApi fileStorage;

    public UserDto toUserDto(User user) {
        return new UserDto(user.getId().toString(), null, user.getAccountSetup(), user.getUsername(), user.getAvatar() != null ? fileStorage.getUrl(user.getAvatar()) : null, user.getCreatedAt());
    }

    public List<UserDto> toUserDtos(List<User> users) {
        List<UserDto> userDtos = new ArrayList<>();
        users.forEach(user -> userDtos.add(toUserDto(user)));
        return userDtos;
    }
}
