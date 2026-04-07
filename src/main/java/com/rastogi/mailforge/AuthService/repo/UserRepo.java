package com.rastogi.mailforge.AuthService.repo;

import com.rastogi.mailforge.AuthService.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,String> {


    Optional<User> findByUsername( String username);

    Optional<User> findUserById(String userId);

}
