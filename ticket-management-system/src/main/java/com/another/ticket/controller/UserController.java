package com.another.ticket.controller;

import com.another.ticket.entity.DTO.UserRegDTO;
import com.another.ticket.entity.Users;
import com.another.ticket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegDTO userRegDTO) {
        return userService.registerUser(userRegDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Users> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.getUserById(id));
        } catch (ChangeSetPersister.NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<Users> getByName(@RequestParam(name = "username") String username) {
        try {
            return ResponseEntity.ok(userService.getUserByName(username));
        } catch (ChangeSetPersister.NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUserById(@PathVariable Long id) {
        userService.deleteUserByName(id);
    }

    @GetMapping("/report/period/{start}/{end}")
    public void getCreateUserReportForPeriod(@PathVariable String start,
                                             @PathVariable String end,
                                             Principal principal) {
        userService.getCreateUserReportForPeriod(start, end, principal);
    }

    @GetMapping("/report/efficiency/{username}")
    public void getEfficiencyUserReport(@PathVariable String username,
                                             Principal principal) {
        userService.getEfficiencyUserReport(username, principal);
    }
}







