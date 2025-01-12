package com.another.ticket.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(s ->
                        s.requestMatchers("/users/delete/{id}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/users/register").permitAll()
                                .requestMatchers("/users/{id}").authenticated()
                                .requestMatchers(HttpMethod.GET, "/users").authenticated()
                                .requestMatchers("/tasks/report/**", "/users/report/**").hasRole("ADMIN")
                                .requestMatchers("/tasks/create", "/tasks/taskAcceptanceConfirmation/{id}",
                                        "/conf-task").hasAnyRole("CLIENT", "ADMIN")
                                .requestMatchers("/tasks/take-work/{id}", "tasks/set-status/{id}").hasAnyRole("PERFORMER", "ADMIN")
                                .requestMatchers("/tasks/find", "/tasks/my").authenticated()
                                .requestMatchers("/tasks/{id}").authenticated()
                                .requestMatchers("/error").permitAll()
                                .anyRequest().permitAll()
                )
                .formLogin(Customizer.withDefaults())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
