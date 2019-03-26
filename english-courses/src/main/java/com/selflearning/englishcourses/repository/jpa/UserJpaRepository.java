package com.selflearning.englishcourses.repository.jpa;

import com.selflearning.englishcourses.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<User, UUID> {

    @Query("SELECT user FROM User user WHERE user.username = :user OR user.email = :user")
    Optional<User> findByUsernameOrEmail(@Param("user") String user);

}
