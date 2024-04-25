package org.example.testtaskclearsolutions.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.testtaskclearsolutions.dto.*;
import org.example.testtaskclearsolutions.exception.RegistrationException;
import org.example.testtaskclearsolutions.mapper.UserMapper;
import org.example.testtaskclearsolutions.model.Role;
import org.example.testtaskclearsolutions.model.User;
import org.example.testtaskclearsolutions.repository.RoleRepository;
import org.example.testtaskclearsolutions.repository.UserRepository;
import org.example.testtaskclearsolutions.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.example.testtaskclearsolutions.exception.EntityNotFoundException;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Value("${age.limit}")
    private int MIN_AGE;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;


    @Override
    public UserResponseDto register(UserRegistrationRequestDto request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new RegistrationException("Such email already exists : " + request.email());
        }
        checkUserAge(request);

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRoles(new HashSet<>(Arrays.asList(
                roleRepository.findByName(Role.RoleName.ROLE_USER)
        )));
        userRepository.save(user);
        return userMapper.toUserResponseDto(user);

    }

    @Override
    public void deleteById(Long id) {
        isUserExistById(id);
        userRepository.deleteById(id);
    }

    @Override
    public List<UserResponseDto> search(UserSearchRequestDto request) {
        isFromDateLessThanToDate(request);
        return userRepository.findUsersByBirthDateBetween(request.fromDate(), request.toDate())
                .stream()
                .map(userMapper::toUserResponseDto)
                .toList();
    }


    @Override
    public UserResponseDto fullUpdate(UserFullUpdateRequestDto request, Long id) {
        checkUserAge(request);
        User user = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User with id " + id + " not found")
        );
        userMapper.updateUser(request, user);
        return userMapper.toUserResponseDto(userRepository.save(user));
    }

    @Override
    public UserResponseDto particularUpdate(UserPatchtUpdateRequestDto request, Long id) {
        checkUserAge(request);
        User user = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User with id " + id + " not found")
        );
        reflection(request, user);
        return userMapper.toUserResponseDto(userRepository.save(user));
    }

    private static void reflection(UserPatchtUpdateRequestDto request, User user) {
        ReflectionUtils.doWithFields(UserPatchtUpdateRequestDto.class, field -> {
            field.setAccessible(true);
            Object value = field.get(request);

            if (value != null) {
                Field target = ReflectionUtils.findField(User.class, field.getName());
                if (target != null) {
                    target.setAccessible(true);
                    ReflectionUtils.setField(target, user, value);
                }
            }
        });
    }

    private void checkUserAge(UserRegistrationRequestDto request) {
        if (!(Period.between(request.birthDate(), LocalDate.now()).getYears() >= MIN_AGE)) {
            throw new RegistrationException("You are too young , wait for 18 )");
        }
    }
    private void checkUserAge(UserFullUpdateRequestDto request) {
        if (!(Period.between(request.birthDate(), LocalDate.now()).getYears() >= MIN_AGE)) {
            throw new RegistrationException("You are too young , wait for 18 )");
        }
    }
    private void checkUserAge(UserPatchtUpdateRequestDto request) {
        if (!(Period.between(request.birthDate(), LocalDate.now()).getYears() >= MIN_AGE)) {
            throw new RegistrationException("You are too young , wait for 18 )");
        }
    }

    private void isUserExistById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("Such user does not exist" + id);
        }
    }

    private void isFromDateLessThanToDate(UserSearchRequestDto request) {
        if (request.fromDate().isAfter(request.toDate())) {
            throw new IllegalArgumentException("From date is after to date date");
        }
    }

}
