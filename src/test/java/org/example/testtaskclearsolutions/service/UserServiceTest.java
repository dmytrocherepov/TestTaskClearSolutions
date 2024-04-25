package org.example.testtaskclearsolutions.service;

import org.example.testtaskclearsolutions.dto.*;
import org.example.testtaskclearsolutions.exception.EntityNotFoundException;
import org.example.testtaskclearsolutions.exception.RegistrationException;
import org.example.testtaskclearsolutions.mapper.UserMapper;
import org.example.testtaskclearsolutions.mapper.impl.UserMapperImpl;
import org.example.testtaskclearsolutions.model.Role;
import org.example.testtaskclearsolutions.model.User;
import org.example.testtaskclearsolutions.repository.RoleRepository;
import org.example.testtaskclearsolutions.repository.UserRepository;
import org.example.testtaskclearsolutions.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.example.testtaskclearsolutions.model.Role.RoleName.ROLE_USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @Spy
    private UserMapper userMapper = new UserMapperImpl();

    @InjectMocks
    private UserServiceImpl userService;


    @Test
    @Description("Create a user")
    void save_ValidRegistrationRequestDto_returnsValidDto() {
        UserRegistrationRequestDto request = new UserRegistrationRequestDto(
                "123",
                "123",
                "test@gmail.com",
                "test",
                "test",
                null,
                LocalDate.of(2010, 6, 25),
                null
        );

        User user = new User();
        Role role = new Role();
        role.setName(ROLE_USER);
        user.setRoles(Set.of(role));


        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(passwordEncoder.encode(request.password())).thenReturn("encoded");
        when(roleRepository.findByName(ROLE_USER)).thenReturn(role);
        when(userRepository.save(any(User.class))).thenReturn(user);


        UserResponseDto response = userService.register(request);

        assertThat(response)
                .hasFieldOrPropertyWithValue("email", request.email())
                .hasFieldOrPropertyWithValue("firstName", request.firstName())
                .hasFieldOrPropertyWithValue("lastName", request.lastName())
                .hasFieldOrPropertyWithValue("birthDate", request.birthDate());
    }


    @Test
    @Description("Deletes a user")
    void deleteById_ValidId_ShouldDoNothing() {
        Long id = 1L;
        User user = new User();
        user.setId(id);

        when(userRepository.existsById(id)).thenReturn(true);
        userService.deleteById(id);

        verify(userRepository, times(1)).existsById(id);
        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    @Description("Update all fiels of User")
    void updateById_ValidUpdateRequestDto_returnsValidDto() {
        Long id = 1L;
        User user = new User();
        user.setId(id);
        user.setFirstName("test1");
        user.setLastName("test2");

        UserFullUpdateRequestDto request = new UserFullUpdateRequestDto(
                "test",
                "test",
                null,
                LocalDate.of(2003, 7, 13),
                null
        );

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDto response = userService.fullUpdate(request, user.getId());

        assertThat(response)
                .hasFieldOrPropertyWithValue("firstName", request.firstName())
                .hasFieldOrPropertyWithValue("lastName", request.lastName());


    }

    @Test
    @Description("Update all fiels of User")
    void particularUpdate_ValidUpdateRequestDto_returnsValidDto() {
        Long id = 1L;
        User user = new User();
        user.setId(id);
        user.setFirstName("test1");
        user.setLastName("test2");
        user.setAddress("testAddress");
        user.setBirthDate(LocalDate.of(2002, 6, 12));

        UserPatchtUpdateRequestDto request = new UserPatchtUpdateRequestDto(
                "test",
                "test",
                null,
                LocalDate.of(2003, 7, 13),
                null
        );

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDto response = userService.particularUpdate(request, user.getId());

        assertThat(response)
                .hasFieldOrPropertyWithValue("firstName", request.firstName())
                .hasFieldOrPropertyWithValue("lastName", request.lastName())
                .hasFieldOrPropertyWithValue("address", user.getAddress());
    }

    @Test
    void search_ValidRequest_ReturnsValidDto() {
        UserSearchRequestDto request = new UserSearchRequestDto(
                LocalDate.of(2000, 11, 7),
                LocalDate.of(2006, 12, 5)
        );
        User user = new User();
        when(userRepository.findUsersByBirthDateBetween(request.fromDate(), request.toDate())).thenReturn(List.of(user));

        List<UserResponseDto> response = userService.search(request);

        assertNotNull(response);
        assertEquals(1, response.size());
    }


    @Test
    void register_InvalidRequestWithExistingEmail_ShouldThrowException() {
        UserRegistrationRequestDto request = new UserRegistrationRequestDto(
                "123",
                "123",
                "test@gmail.com",
                null,
                "test",
                null,
                LocalDate.of(2010, 6, 25),
                null
        );

        when(userRepository.existsByEmail(request.email())).thenReturn(true);

        assertThatThrownBy(() -> userService.register(request))
                .isInstanceOf(RegistrationException.class)
                .hasMessage("Such email already exists : " + request.email());
    }

    @Test
    void fullUpdate_InvalidId_ShouldThrowException() {
        Long id = -1L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        UserFullUpdateRequestDto request = new UserFullUpdateRequestDto(
                "123",
                "123",
                "test@gmail.com",
                LocalDate.of(2000,2,13),
                null
        );

        assertThatThrownBy(() -> userService.fullUpdate(request, id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("User with id " + id + " not found");
    }

    @Test
    void particularUpdate_InvalidId_ShouldThrowException() {
        Long id = -1L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        UserPatchtUpdateRequestDto request = new UserPatchtUpdateRequestDto(
                "123",
                "123",
                "test@gmail.com",
                LocalDate.of(2000,2,13),
                null
        );

        assertThatThrownBy(() -> userService.particularUpdate(request, id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("User with id " + id + " not found");
    }
}
