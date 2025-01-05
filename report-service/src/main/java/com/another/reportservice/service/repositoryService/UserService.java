package com.another.reportservice.service.repositoryService;

import com.another.reportservice.entity.Users;
import com.another.reportservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<Users> getAllUsers() {
        List<Users> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    public List<Users> getUserBetweenDate(LocalDate start, LocalDate end) {
        return userRepository.findAllByCreateDataBetween(start, end);
    }

    public Users getUserByUsername(String username) throws ChangeSetPersister.NotFoundException {
        return userRepository.findByUsername(username).orElseThrow(ChangeSetPersister.NotFoundException::new);
    }
}
