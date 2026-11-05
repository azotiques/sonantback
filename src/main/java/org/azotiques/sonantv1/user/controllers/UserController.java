package org.azotiques.sonantv1.user.controllers;

import lombok.RequiredArgsConstructor;
import org.azotiques.sonantv1.user.UserDto;
import org.azotiques.sonantv1.user.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserDto> getUser(@AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(userService.getUser(jwt), HttpStatus.OK);
    }

}
