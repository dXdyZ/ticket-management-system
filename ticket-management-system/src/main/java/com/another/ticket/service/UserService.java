package com.another.ticket.service;

import com.another.ticket.entity.DTO.UserRegDTO;
import com.another.ticket.entity.Role;
import com.another.ticket.entity.Users;
import com.another.ticket.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> registerUser(UserRegDTO userRegDTO) {
        Optional<Users> users = userRepository.findByUsername(userRegDTO.getUsername());
        if (users.isEmpty() && !userRegDTO.getRole().equals(Role.ROLE_ADMIN)) {
            return ResponseEntity.ok(userRepository.save(Users.builder()
                    .username(userRegDTO.getUsername())
                    .email(userRegDTO.getEmail())
                    .role(userRegDTO.getRole())
                    .password(userRegDTO.getPassword())
                    .createData(new Date())
                    .build()));
        } else return new ResponseEntity<>("Пользователь с таким именем уже существует", HttpStatus.CONFLICT);
    }

    public Users getUserByPrincipal(Principal principal) {
        return userRepository.findByUsername(principal.getName()).get();
    }

    public void deleteUserByName(Long id) {
        userRepository.deleteById(id);
    }

    public Users getUserById(Long id) throws ChangeSetPersister.NotFoundException {
        return userRepository.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new);
    }

    public Users getUserByName(String username) throws ChangeSetPersister.NotFoundException {
        return userRepository.findByUsername(username).orElseThrow(ChangeSetPersister.NotFoundException::new);
    }
}
