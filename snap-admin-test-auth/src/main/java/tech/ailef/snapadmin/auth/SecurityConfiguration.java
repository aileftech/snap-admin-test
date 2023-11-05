package tech.ailef.snapadmin.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
public class SecurityConfiguration {
	@Bean
	MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
		return new MvcRequestMatcher.Builder(introspector);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	@Bean
	UserDetailsService userDetailsService() {
		return new DatabaseUserDetailsService();
	}

	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
	    return http.authorizeHttpRequests(auth -> {
	        // POST methods (create, edit and delete) require ADMIN role
	        auth.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/admin/**")).hasAuthority("ADMIN")
	            // Read-only SnapAdmin routes require authentication (any role)
	            .requestMatchers(AntPathRequestMatcher.antMatcher("/admin/**")).authenticated()
	            // The other routes are not protected (adapt to your needs)
	            .requestMatchers(AntPathRequestMatcher.antMatcher("/**")).permitAll();
	    })
	    .formLogin(l -> l.loginPage("/login").permitAll())
	    .build();
	}
}
