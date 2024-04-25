package org.example.testtaskclearsolutions.service;

import org.example.testtaskclearsolutions.dto.*;

import java.util.List;

public interface UserService {

    UserResponseDto register(UserRegistrationRequestDto request);

    void deleteById(Long id);

    List<UserResponseDto> search(UserSearchRequestDto request);

    UserResponseDto fullUpdate(UserFullUpdateRequestDto request ,Long id);

    UserResponseDto particularUpdate(UserPatchtUpdateRequestDto request , Long id);
}
