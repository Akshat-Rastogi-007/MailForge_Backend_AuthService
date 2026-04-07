package com.rastogi.mailforge.AuthService.security.user;

import com.rastogi.mailforge.AuthService.entity.User;
import com.rastogi.mailforge.AuthService.error.errors.ResourceNotFoundException;
import com.rastogi.mailforge.AuthService.repo.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepo userRepo;

    public CustomUserDetailService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(username));

        return new CustomUserDetails(user);
    }

}
