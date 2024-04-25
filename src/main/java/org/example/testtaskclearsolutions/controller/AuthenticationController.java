package org.example.testtaskclearsolutions.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.testtaskclearsolutions.dto.UserLoginRequestDto;
import org.example.testtaskclearsolutions.dto.UserLoginResponseDto;
import org.example.testtaskclearsolutions.dto.UserRegistrationRequestDto;
import org.example.testtaskclearsolutions.dto.UserResponseDto;
import org.example.testtaskclearsolutions.security.AuthenticationService;
import org.example.testtaskclearsolutions.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/register")
    public UserResponseDto register(@RequestBody @Valid UserRegistrationRequestDto request) {
        return userService.register(request);
    }

    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto request) {
        return authenticationService.authenticate(request);
    }

}
