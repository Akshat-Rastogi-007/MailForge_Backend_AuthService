package com.rastogi.mailforge.AuthService.controller.publicController;

import com.rastogi.mailforge.AuthService.dto.request.LoginDto;
import com.rastogi.mailforge.AuthService.dto.response.ApiResponseDto;
import com.rastogi.mailforge.AuthService.dto.response.LoginResponseDto;
import com.rastogi.mailforge.AuthService.service.login.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/v1/public/")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto, HttpServletRequest request,
            HttpServletResponse response) {

        LoginResponseDto login = loginService.login(loginDto, request, response);

        return new ResponseEntity<>(

                new ApiResponseDto<>("", login, HttpStatus.OK), HttpStatus.OK);

    }

}
