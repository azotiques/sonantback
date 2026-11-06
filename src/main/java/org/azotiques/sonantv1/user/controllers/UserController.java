package org.azotiques.sonantv1.user.controllers;

import lombok.RequiredArgsConstructor;
import org.azotiques.sonantv1.user.UserDto;
import org.azotiques.sonantv1.user.dtos.EditUserRequest;
import org.azotiques.sonantv1.user.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserDto> getUser(@AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(userService.getUser(jwt), HttpStatus.OK);
    }

    @PutMapping(value = "/user/edit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDto> editUser(@AuthenticationPrincipal Jwt jwt, @ModelAttribute EditUserRequest editUserRequest) {
        return new ResponseEntity<>(userService.editUser(jwt, editUserRequest), HttpStatus.OK);
    }

}
