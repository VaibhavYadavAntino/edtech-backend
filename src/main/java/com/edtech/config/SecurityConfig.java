package com.edtech.config;

import com.edtech.repository.UserRepository;
import com.edtech.util.JwtUtil;
import com.edtech.config.JwtAuthFilter;
import com.edtech.exception.UnauthorizedAccessException;

import lombok.RequiredArgsConstructor;


import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.*;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtUtil jwtUtil;
	private final UserRepository userRepository;
	private final UnauthorizedAccessException accessDeniedHandler; 


	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    return http
	        .cors(Customizer.withDefaults())
	        .csrf(csrf -> csrf.disable())
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers("/auth/**").permitAll()
	            .requestMatchers(HttpMethod.POST, "/api/courses/**").hasRole("INSTRUCTOR")
	            .requestMatchers(HttpMethod.PUT, "/api/courses/**").hasRole("INSTRUCTOR")
	            .requestMatchers(HttpMethod.DELETE, "/api/courses/**").hasRole("INSTRUCTOR")
	            .requestMatchers(HttpMethod.GET, "/api/courses/**").permitAll()
	            .anyRequest().authenticated()
	        )
	        .exceptionHandling(exception -> exception
	            .accessDeniedHandler(accessDeniedHandler) 
	        )
	        .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	        .userDetailsService(userDetailsService())
	        .addFilterBefore(new JwtAuthFilter(jwtUtil, userRepository), UsernamePasswordAuthenticationFilter.class)
	        .build();
	}


	@Bean
	public UserDetailsService userDetailsService() {
		return login -> userRepository.findByEmailOrPhoneNumber(login, login)
				.map(user -> org.springframework.security.core.userdetails.User.builder().username(user.getEmail())
						.password(user.getPassword()).roles(user.getRole().name()).build())
				.orElseThrow(() -> new UsernameNotFoundException("User not found with email or phone: " + login));
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
}
