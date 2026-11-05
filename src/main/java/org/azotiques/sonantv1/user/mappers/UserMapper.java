package org.azotiques.sonantv1.user.mappers;

import org.azotiques.sonantv1.user.UserDto;
import org.azotiques.sonantv1.user.entities.User;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {
    static public UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getUsername(), user.getAvatar(), user.getCreatedAt());
    }

    static public List<UserDto> toUserDtos(List<User> users) {
        List<UserDto> userDtos = new ArrayList<>();
        users.forEach(user -> userDtos.add(toUserDto(user)));
        return userDtos;
    }
}
