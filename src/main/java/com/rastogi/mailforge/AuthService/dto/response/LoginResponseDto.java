package com.rastogi.mailforge.AuthService.dto.response;

import com.rastogi.mailforge.AuthService.dto.response.user.UserResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {

    private String status;      // JWT or WAITING_APPROVAL
    private String token;
    private UserResponseDto responseDto;
    private String requestId;

    public static LoginResponseDto jwt(String token,UserResponseDto responseDto) {
        LoginResponseDto dto = new LoginResponseDto();
        dto.status = "SUCCESS";
        dto.token = token;
        dto.responseDto = responseDto;
        return dto;
    }

    public static LoginResponseDto waiting(String requestId, UserResponseDto responseDto) {
        LoginResponseDto dto = new LoginResponseDto();
        dto.status = "WAITING_APPROVAL";
        dto.requestId = requestId;
        return dto;
    }

}
