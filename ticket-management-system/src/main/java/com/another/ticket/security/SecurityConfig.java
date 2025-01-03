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
                        s.requestMatchers("/user/delete/{id}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/user/register").permitAll()
                                .requestMatchers("/user/{id}").authenticated()
                                .requestMatchers(HttpMethod.GET, "/user").authenticated()
                                .requestMatchers("/task/create", "/task/taskAcceptanceConfirmation/{id}",
                                        "/conf-task").hasAnyRole("CLIENT", "ADMIN")
                                .requestMatchers("/task/get-work/{id}", "task/set-status/{id}").hasAnyRole("PERFORMER", "ADMIN")
                                .requestMatchers("/task/find", "/task/my").authenticated()
                                .requestMatchers("/task/{id}").authenticated()
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
