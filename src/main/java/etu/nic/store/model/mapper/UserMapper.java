package etu.nic.store.model.mapper;

import etu.nic.store.model.dto.SignUpRequest;
import etu.nic.store.model.dto.UserDto;
import etu.nic.store.model.entity.User;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
public class UserMapper {
    public UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail().toLowerCase());
        userDto.setRole(user.getRole());
        userDto.setCreatedTime(user.getCreatedTime());
        userDto.setCreatedTime(user.getBlockedTime());
        return userDto;
    }

    public User fromSignUpRequest(SignUpRequest signUpRequest) {
        User user = new User();
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setEmail(signUpRequest.getEmail().toLowerCase());
        user.setPassword(signUpRequest.getPassword().getBytes());
        user.setCreatedTime(OffsetDateTime.now());
        user.setBlockedTime(null);
        return user;
    }

    public User toEntity(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail().toLowerCase());
        user.setRole(userDto.getRole());
        user.setCreatedTime(userDto.getCreatedTime());
        user.setBlockedTime(userDto.getBlockedTime());
        return user;
    }
}
