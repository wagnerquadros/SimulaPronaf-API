package com.wagnerquadros.simulapronaf.infraestrutura.configuracao.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextHolderFilter;

@Configuration
public class SecurityConfig {

    private final RateLimitAuthGoogleFilter rateLimitAuthGoogleFilter;

    public SecurityConfig(RateLimitAuthGoogleFilter rateLimitAuthGoogleFilter) {
        this.rateLimitAuthGoogleFilter = rateLimitAuthGoogleFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/auth/google").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(rateLimitAuthGoogleFilter, SecurityContextHolderFilter.class)
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(Customizer.withDefaults())
                );

        return http.build();
    }
}
