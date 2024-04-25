package org.example.testtaskclearsolutions.mapper;

import org.example.testtaskclearsolutions.config.MapperConfig;
import org.example.testtaskclearsolutions.dto.UserFullUpdateRequestDto;
import org.example.testtaskclearsolutions.dto.UserRegistrationRequestDto;
import org.example.testtaskclearsolutions.dto.UserResponseDto;
import org.example.testtaskclearsolutions.model.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    @Mapping(source = "birthDate" , target = "birthDate")
    @Mapping(source = "phoneNumber" , target = "phoneNumber")
    User toUser(UserRegistrationRequestDto user);

    UserResponseDto toUserResponseDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUser(
            UserFullUpdateRequestDto updateBookRequestDto, @MappingTarget User user
    );
}
