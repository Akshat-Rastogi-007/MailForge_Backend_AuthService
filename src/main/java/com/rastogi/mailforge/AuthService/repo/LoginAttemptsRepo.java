package com.rastogi.mailforge.AuthService.repo;

import com.rastogi.mailforge.AuthService.entity.LoginAttempts;
import com.rastogi.mailforge.AuthService.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginAttemptsRepo extends JpaRepository<LoginAttempts,String> {
    

}
