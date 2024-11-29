package etu.nic.store.model.dto;

import com.sun.istack.NotNull;
import etu.nic.store.model.enums.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.OffsetDateTime;


@Data
@NoArgsConstructor
public class UserDto {
    private Long id;

    @NotBlank(message = "Имя пользователя не должно быть пустым")
    private String firstName;

    @NotBlank(message = "Фамилия пользователя не должно быть пустым")
    private String lastName;

    @NotBlank(message = "Email не должен быть пустым")
    @Email(message = "Некорректный формат email")
    private String email;

    @NotNull
    private Role role;

    private OffsetDateTime createdTime;

    private OffsetDateTime blockedTime;

    @Override
    public String toString() {
        return id + " " + role;
    }
}


