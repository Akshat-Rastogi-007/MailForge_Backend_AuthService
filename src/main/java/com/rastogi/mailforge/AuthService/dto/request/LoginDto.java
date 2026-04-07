package com.rastogi.mailforge.AuthService.dto.request;


import lombok.Data;

@Data
public class LoginDto {
    private String username;
    private String password;
    private Boolean saveDevice;
}
