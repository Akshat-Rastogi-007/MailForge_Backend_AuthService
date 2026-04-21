package com.rastogi.mailforge.AuthService.security;

import com.rastogi.mailforge.AuthService.security.filter.JwtAuthFilter;
import com.rastogi.mailforge.AuthService.security.user.CustomUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security Configuration for Auth Service
 * - Public: registration, login, health, swagger
 * - Authenticated: SSE, user profile operations
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailService customUserDetailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(CustomUserDetailService customUserDetailService,
            PasswordEncoder passwordEncoder,
            JwtAuthFilter jwtAuthFilter) {
        this.customUserDetailService = customUserDetailService;
        this.passwordEncoder = passwordEncoder;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(request -> request
                        // Health & monitoring
                        .requestMatchers("/actuator/**", "/health").permitAll()
                        // Swagger / OpenAPI
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        // Public auth endpoints — registration, login, OTP
                        .requestMatchers("/app/v1/public/**").permitAll()
                        .requestMatchers("/app/v1/user/create", "/app/v1/user/verify", "/app/v1/user/resend-otp").permitAll()
                        // Protected user endpoints — require JWT
                        .requestMatchers("/app/v1/user/**").authenticated()
                        // SSE — require JWT
                        .requestMatchers("/app/v1/sse/**").authenticated()
                        // Everything else — require JWT
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(authenticationProvider());
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(customUserDetailService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
}
