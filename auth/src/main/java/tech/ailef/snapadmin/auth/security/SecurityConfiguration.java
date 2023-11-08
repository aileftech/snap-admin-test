package tech.ailef.snapadmin.auth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import tech.ailef.snapadmin.external.SnapAdminProperties;

@Configuration
public class SecurityConfiguration {
	@Autowired
	private SnapAdminProperties properties;
	
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
		String baseUrl = properties.getBaseUrl();
		
	    return http.authorizeHttpRequests(auth -> {
	        // POST methods (create, edit and delete) require ADMIN role
	    	// Note that with this configuration users will still be able to access the edit/create page
	    	// but they will get a Forbidden error after submitting the form if they are not authorized.
	    	// You can also stop the serving of these pages altogether by customizing the route matchers
	        auth.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/" + baseUrl + "/**")).hasAuthority("ADMIN")
	            // Read-only SnapAdmin routes require authentication (any role)
	            .requestMatchers(AntPathRequestMatcher.antMatcher("/" + baseUrl + "/**")).authenticated()
	            // The other routes are not protected (adapt to your needs)
	            .requestMatchers(AntPathRequestMatcher.antMatcher("/**")).permitAll();
	    })
	    .formLogin(l -> l.loginPage("/login").permitAll())
	    /*
	     * This custom exception handling code is only needed if you want to have
	     * nicer Forbidden error pages, for cases when a user tries to perform an
	     * action they don't have the correct privileges for.
	     * For example, in the previous configuration a user without "ADMIN" role
	     * trying to edit/create items.
	     * The exception handling is delegated to the default handler if the
	     * error didn't occur on a SnapAdmin route. You can further customize this
	     * according to your need or ignore it entirely. In this last scenario, your 
	     * default Forbidden page will be shown even for errors occurring inside
	     * SnapAdmin.
	     */
	    .exceptionHandling(e -> e.accessDeniedHandler((req, res, ex) -> {
	    	AccessDeniedHandlerImpl defaultHandler = new AccessDeniedHandlerImpl();
	    	if (req.getServletPath().toString().startsWith("/" + baseUrl + "/")) {
	    		res.sendRedirect("/" + baseUrl + "/forbidden");
	    	} else {
	    		defaultHandler.handle(req, res, ex);
	    	}
	    }))
	    .build();
	}
}
