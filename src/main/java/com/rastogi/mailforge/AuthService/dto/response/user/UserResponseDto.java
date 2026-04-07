package com.rastogi.mailforge.AuthService.dto.response.user;

import com.rastogi.mailforge.AuthService.enums.AccountStatus;
import lombok.Data;

@Data
public class UserResponseDto {

    private String id;
    private String username;
    private AccountStatus status;

}
