package tech.ailef.snapadmin.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.ailef.snapadmin.auth.models.User;

public interface AuthUserRepository extends JpaRepository<User, Integer> {
	public Optional<User> findByUsername(String username);
}
