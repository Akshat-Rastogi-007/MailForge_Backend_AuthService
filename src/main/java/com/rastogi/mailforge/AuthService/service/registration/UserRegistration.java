package com.rastogi.mailforge.AuthService.service.registration;

import com.rastogi.mailforge.AuthService.dto.request.UserDto;
import com.rastogi.mailforge.AuthService.dto.response.user.UserResponseDto;
import com.rastogi.mailforge.AuthService.entity.User;
import com.rastogi.mailforge.AuthService.enums.AccountStatus;
import com.rastogi.mailforge.AuthService.enums.Roles;
import com.rastogi.mailforge.AuthService.error.errors.ResourceAlreadyExistsException;
import com.rastogi.mailforge.AuthService.repo.UserRepo;
import com.rastogi.mailforge.AuthService.service.kafka.UserRegistrationEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserRegistration {


    private final UserRepo userRepo;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRegistrationEventPublisher eventPublisher;

    public UserRegistration(UserRepo userRepo, ModelMapper modelMapper,
                           PasswordEncoder passwordEncoder, UserRegistrationEventPublisher eventPublisher) {
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
    }


    @Transactional
    public UserResponseDto registerUser(UserDto userDto) {

        if (userRepo.findByUsername(userDto.getUsername()).isPresent()) {
            throw new ResourceAlreadyExistsException(userDto.getUsername());
        }

        User mappedUser = modelMapper.map(userDto, User.class);

        mappedUser.setPassword(passwordEncoder.encode(userDto.getPassword()));

        mappedUser.getRoles().add(Roles.ROLE_USER);
        mappedUser.setStatus(AccountStatus.ACTIVE);

        User save = userRepo.save(mappedUser);
        
        // Publish user registration event to Kafka for downstream services (User Service, Mail Service, etc.)
        log.info("Publishing user registration event for username: {}", userDto.getUsername());
        eventPublisher.publishUserRegistrationEvent(save);

        return modelMapper.map(save, UserResponseDto.class);
    }

}