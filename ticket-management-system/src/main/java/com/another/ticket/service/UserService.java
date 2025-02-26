package com.another.ticket.service;

import com.another.ticket.entity.DTO.RequestReportDTO;
import com.another.ticket.entity.DTO.UserRegDTO;
import com.another.ticket.entity.UserBot;
import com.another.ticket.entity.Users;
import com.another.ticket.rabbit.RabbitMessage;
import com.another.ticket.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
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
import java.time.LocalDate;
import java.util.Optional;


@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RabbitMessage rabbitMessage;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                      RabbitMessage rabbitMessage) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.rabbitMessage = rabbitMessage;
    }

    public ResponseEntity<?> registerUser(UserRegDTO userRegDTO) {
        Optional<Users> users = userRepository.findByUsername(userRegDTO.getUsername());
        if (users.isEmpty()) {
            return new ResponseEntity<>(userRepository.save(Users.builder()
                    .username(userRegDTO.getUsername())
                    .email(userRegDTO.getEmail())
                    .role(userRegDTO.getRole())
                    .password(passwordEncoder.encode(userRegDTO.getPassword()))
                    .createData(LocalDate.now())
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
        rabbitMessage.sendRequestReportMessage(RequestReportDTO.builder()
                        .start(start)
                        .end(end)
                        .email(getUserByPrincipal(principal).getEmail())
                .build(), "user_period");
    }

    public void getEfficiencyUserReport(String username, Principal principal) {
        rabbitMessage.sendRequestReportMessage(RequestReportDTO.builder()
                        .email(getUserByPrincipal(principal).getEmail())
                        .username(username)
                .build(), "user_efficiency");
    }
}
