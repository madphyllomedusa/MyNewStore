package etu.nic.store.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {
    @NotBlank(message = "Email не должен быть пустым")
    @Email(message = "Некорректный формат email")
    private String email;

    @NotBlank(message = "Имя пользователя не должно быть пустым")
    private String firstName;

    @NotBlank(message = "Фамилия пользователя не должно быть пустым")
    private String lastName;

    @NotNull(message = "Пароль не должен быть пустым")
    private String password;

    @NotBlank(message = "Подтверждение пароля не должно быть пустым")
    private String confirmPassword;

    @Override
    public String toString() {
        return email;
    }
}
