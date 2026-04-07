package com.rastogi.mailforge.AuthService.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Data
public class ApiResponseDto<T> {

    private String message;
    private T data;
    private HttpStatus status;

}
