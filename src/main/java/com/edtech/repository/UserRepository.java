package com.edtech.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edtech.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{
	Optional<User> findByEmailOrPhoneNumber(String email, String phoneNumber);
}
