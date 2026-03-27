package com.tss.loanEmiSchedular.repository;

import com.tss.loanEmiSchedular.entity.User;
import com.tss.loanEmiSchedular.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByRole(Role role);
}