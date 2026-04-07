package com.rastogi.mailforge.AuthService.service.login;

import com.rastogi.mailforge.AuthService.dto.request.LoginDto;
import com.rastogi.mailforge.AuthService.dto.response.LoginResponseDto;
import com.rastogi.mailforge.AuthService.dto.response.user.UserResponseDto;
import com.rastogi.mailforge.AuthService.entity.User;
import com.rastogi.mailforge.AuthService.enums.AccountStatus;
import com.rastogi.mailforge.AuthService.error.errors.UserNotActiveException;
import com.rastogi.mailforge.AuthService.repo.UserRepo;
import com.rastogi.mailforge.AuthService.security.jwt.JwtUtil;
import com.rastogi.mailforge.AuthService.security.user.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final UserRepo userRepo;
    private final AuthenticationManager manager;
    private final JwtUtil jwtUtil;
    private final ModelMapper modelMapper;

    public LoginService(UserRepo userRepo, AuthenticationManager manager, JwtUtil jwtUtil, ModelMapper modelMapper) {
        this.userRepo = userRepo;
        this.manager = manager;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public LoginResponseDto login(LoginDto loginDto, HttpServletRequest request, HttpServletResponse response) {

        User user = authenticateAndValidate(loginDto);

        CustomUserDetails userDetails = new CustomUserDetails(user);

        UserResponseDto mapResponse = modelMapper.map(user, UserResponseDto.class);

        String token = jwtUtil.getJwt(userDetails, 15);

        return LoginResponseDto.jwt(token, mapResponse);

    }
    private User authenticateAndValidate(LoginDto loginDto) {
        Authentication authenticate = authenticate(loginDto.getUsername(), loginDto.getPassword());

        if (!authenticate.isAuthenticated()) {
            throw new BadCredentialsException("Invalid credentials");
        }
        CustomUserDetails userDetails = (CustomUserDetails) authenticate.getPrincipal();

        if (userDetails == null) {
            throw new BadCredentialsException("User not found");
        }

        User user = userDetails.getUser();

        if (user.getStatus() != AccountStatus.ACTIVE) {
            throw new UserNotActiveException("Account not active");
        }

        return user;
    }

    private Authentication authenticate(String username, String password) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        return manager.authenticate(token);
    }
}
