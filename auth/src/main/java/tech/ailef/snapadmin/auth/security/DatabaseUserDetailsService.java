package tech.ailef.snapadmin.auth.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import tech.ailef.snapadmin.auth.models.User;
import tech.ailef.snapadmin.auth.repository.AuthUserRepository;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {
	@Autowired
	private AuthUserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findByUsername(username);
		if (user.isPresent()) {
			return new DatabaseUserDetails(user.get());
		} else {
			throw new UsernameNotFoundException("Username not found");
		}
	}
}