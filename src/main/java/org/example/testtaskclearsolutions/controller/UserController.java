package org.example.testtaskclearsolutions.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.testtaskclearsolutions.dto.*;
import org.example.testtaskclearsolutions.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable Long id) {
        userService.deleteById(id);
    }

    @GetMapping
    @RequestMapping("/search")
    public List<UserResponseDto> findAll(@RequestBody @Valid UserSearchRequestDto request) {
        return userService.search(request);
    }

    @PutMapping("/{id}")
    public UserResponseDto updateById(@RequestBody @Valid UserFullUpdateRequestDto request , @PathVariable Long id) {
        return userService.fullUpdate(request , id);
    }

    @PatchMapping("/{id}")
    public UserResponseDto patch(@PathVariable Long id, @RequestBody @Valid UserPatchtUpdateRequestDto request) {
        return userService.particularUpdate(request, id);
    }
}
