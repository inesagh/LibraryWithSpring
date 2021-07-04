package com.example.librarywithspring.controller;

import com.example.librarywithspring.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BookController {
    private BookService service = new BookService();

    @PostMapping(value = "book/create")
    public ResponseEntity<?> create(@RequestHeader(value = "role") String role) {
        try {
            return service.createBooks(role);
        } catch (NullPointerException e) {
            return ResponseEntity.ok(ResponseEntity.status(406));
        }
    }

    @GetMapping(value = "book/read")
    public ResponseEntity<?> read(@RequestHeader(value = "role") String role) {
        try {
            return service.readAllBooks(role);
        } catch (NullPointerException e) {
            return ResponseEntity.ok(ResponseEntity.status(406));
        }
    }

    @GetMapping(value = "book/read/{name}/{surname}")
    public ResponseEntity<?> readByTheAuthor(@RequestHeader(value = "role") String role,
                                             @PathVariable String name,
                                             @PathVariable String surname) {
        try {
            return service.readByTheAuthor(role, name, surname);
        } catch (NullPointerException e) {
            return ResponseEntity.ok(ResponseEntity.status(406));
        }
    }

    @PutMapping(value = "book/update/{title}")
    public ResponseEntity<?> updateTheBook(@RequestHeader(value = "role") String role,
                                           @PathVariable String title) {
        try {
            return service.updateTheBook(role, title);
        } catch (NullPointerException e) {
            return ResponseEntity.ok(ResponseEntity.status(406));
        }
    }

    @DeleteMapping(value = "book/delete/{title}")
    public ResponseEntity<?> deleteTheBook(@RequestHeader(value = "role") String role,
                                           @PathVariable String title) {
        try {
            return service.deleteTheBook(role, title);
        } catch (NullPointerException e) {
            return ResponseEntity.ok(ResponseEntity.status(406));
        }
    }
}
