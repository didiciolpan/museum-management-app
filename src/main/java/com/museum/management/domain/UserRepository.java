package com.museum.management.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {

	List<User> findAll();

	@Query("SELECT u FROM User u WHERE u.username = ?1")
	Optional<User> findByUsername(String username);
	
	@Query("SELECT u FROM User u, Role r where u.role.id = r.id AND r.name = ?1")
	List<User> getUsersByRole(String role);
}
