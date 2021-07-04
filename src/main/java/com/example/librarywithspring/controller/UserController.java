package com.example.librarywithspring.controller;

import com.example.librarywithspring.service.UserService;
import com.example.librarywithspring.userandbook.Person;
import com.example.librarywithspring.userandbook.UserWithoutPassword;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
public class UserController {
    private UserService service = new UserService();

    @PostMapping(value = "registration")
    public ResponseEntity<?> register(@RequestBody Person person) {
        UserWithoutPassword registration = null;
        try {
            registration = service.registration(person);
            return ResponseEntity.ok(registration);
        } catch (NullPointerException e) {
            System.out.println("nullllllllllllll");
            return ResponseEntity.ok(ResponseEntity.status(406));
        }
    }

    @GetMapping(value = "login/{username}/{password}")
    public ResponseEntity<?> login(@PathVariable String username, @PathVariable String password) {
        try {
            return service.login(username, password);
        } catch (NullPointerException e) {
            return ResponseEntity.ok(ResponseEntity.status(406));
        }
    }

    @GetMapping(value = "login/book")
    public ResponseEntity<List<Map<String, String>>> booksForUser() {
        return ResponseEntity.ok(service.currentAvailableBooks());
    }

    @PutMapping(value = "login/book/{title}")
    public ResponseEntity<?> chooseBook(@RequestHeader(value = "username") String username,
                                        @RequestHeader(value = "name") String name,
                                        @RequestHeader(value = "surname") String surname,
                                        @RequestHeader(value = "email") String email,
                                        @RequestHeader(value = "role") String role,
                                        @PathVariable String title) {
        try {
            return service.choosenBook(username, name, surname, email, role, title);
        } catch (NullPointerException e) {
            return ResponseEntity.ok(ResponseEntity.status(406));
        }
    }
}
