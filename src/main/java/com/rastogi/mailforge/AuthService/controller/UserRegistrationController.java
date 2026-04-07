package com.rastogi.mailforge.AuthService.controller;

import com.rastogi.mailforge.AuthService.dto.request.UserDto;
import com.rastogi.mailforge.AuthService.dto.response.ApiResponseDto;
import com.rastogi.mailforge.AuthService.dto.response.user.UserResponseDto;
import com.rastogi.mailforge.AuthService.service.registration.UserRegistration;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/v1/user/")
public class UserRegistrationController {

    private final UserRegistration userRegistration;

    public UserRegistrationController(UserRegistration userRegistration) {
        this.userRegistration = userRegistration;
    }

    @PostMapping("create")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDto userDto){

        UserResponseDto userResponseDto = userRegistration.registerUser(userDto);

        return new ResponseEntity<>(
            new ApiResponseDto<UserResponseDto>("User Registered Successfully",userResponseDto, HttpStatus.OK),
                HttpStatus.OK
        );

    }

}
