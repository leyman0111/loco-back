package ru.leyman.loco.locoback.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.leyman.loco.locoback.domain.dto.Author;
import ru.leyman.loco.locoback.domain.dto.UserDto;
import ru.leyman.loco.locoback.domain.entity.User;

@Mapper
public interface UserMapper {

    UserDto map(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    void map(@MappingTarget User user, UserDto userDto);

    Author mapToAuthor(User user);

}
