package com.another.ticket.service;

import com.another.ticket.entity.DTO.UserRegDTO;
import com.another.ticket.entity.Users;
import com.another.ticket.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;
    private final String mainUrl = "http://report-service/users";

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<?> registerUser(UserRegDTO userRegDTO) {
        Optional<Users> users = userRepository.findByUsername(userRegDTO.getUsername());
        if (users.isEmpty()) {
            return new ResponseEntity<>(userRepository.save(Users.builder()
                    .username(userRegDTO.getUsername())
                    .email(userRegDTO.getEmail())
                    .role(userRegDTO.getRole())
                    .password(passwordEncoder.encode(userRegDTO.getPassword()))
                    .createData(LocalDateTime.now())
                    .build()), HttpStatus.CREATED);
        } else return new ResponseEntity<>("Пользователь с таким именем уже существует", HttpStatus.CONFLICT);
    }

    public Users getUserByPrincipal(Principal principal) {
        return userRepository.findByUsername(principal.getName()).get();
    }

    public void deleteUserByName(Long id) {
        userRepository.deleteById(id);
    }

    public boolean existsUserByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public Users getUserById(Long id) throws ChangeSetPersister.NotFoundException {
        Users users = userRepository.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new);
        users.setPassword(null);
        return users;
    }

    public Users getUserByName(String username) throws ChangeSetPersister.NotFoundException {
        Users users =  userRepository.findByUsername(username).orElseThrow(ChangeSetPersister.NotFoundException::new);
        users.setPassword(null);
        return users;
    }

    public void getCreateUserReportForPeriod(String start, String end, Principal principal) {
        restTemplate.getForObject(
                UriComponentsBuilder.fromUriString(mainUrl)
                .pathSegment("period", start, end, getUserByPrincipal(principal).getEmail())
                    .toUriString(),
                Void.class);
    }

    public ResponseEntity<?> getEfficiencyUserReport(String username, Principal principal) {
        try {
            restTemplate.exchange(
                    UriComponentsBuilder.fromUriString(mainUrl)
                            .pathSegment("efficiency", username, getUserByPrincipal(principal).getEmail())
                            .toUriString(),
                    HttpMethod.GET,
                    null,
                    String.class);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (HttpClientErrorException.NotFound e) {
            if ("User has no completed tasks".equals(e.getResponseBodyAsString())) {
                return new ResponseEntity<>("User has no completed tasks", HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>("User not found: " + username, HttpStatus.NOT_FOUND);
            }
        }
    }
}
