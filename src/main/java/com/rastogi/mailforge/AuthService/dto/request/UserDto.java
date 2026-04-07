package com.rastogi.mailforge.AuthService.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserDto {


    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=]).*$",
            message = "Password must contain uppercase, lowercase, digit and special character"
    )
    private String password;

}
