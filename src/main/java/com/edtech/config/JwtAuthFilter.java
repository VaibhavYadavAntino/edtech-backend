package com.edtech.config;

import com.edtech.repository.UserRepository;
import com.edtech.util.JwtUtil;
import com.edtech.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final UserRepository userRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		final String authHeader = request.getHeader("Authorization");
		final String token;
		final String email;

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			System.out.println("❌ No Bearer token in Authorization header");
			filterChain.doFilter(request, response);
			return;
		}

		token = authHeader.substring(7);

		try {
			email = jwtUtil.extractUsername(token); // this is usually subject (email)
			System.out.println("🔐 Extracted email from token: " + email);
		} catch (Exception e) {
			System.out.println("❌ Failed to extract username from token: " + e.getMessage());
			filterChain.doFilter(request, response);
			return;
		}

		if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			Optional<User> userOpt = userRepository.findByEmailOrPhoneNumber(email, email);

			if (userOpt.isPresent()) {
				User user = userOpt.get();

				if (jwtUtil.validateToken(token, user.getEmail())) {
					UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user.getEmail(),
							null, org.springframework.security.core.authority.AuthorityUtils
									.createAuthorityList("ROLE_" + user.getRole()));

					auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(auth);

					System.out.println("✅ Authenticated user: " + user.getEmail() + " with role: " + user.getRole());
				} else {
					System.out.println("❌ Token validation failed for email: " + email);
				}
			} else {
				System.out.println("❌ No user found for email/phone: " + email);
			}
		}

		filterChain.doFilter(request, response);
	}

}
