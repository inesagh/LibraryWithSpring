package com.example.librarywithspring.service;

import com.example.librarywithspring.DAO;
import com.example.librarywithspring.userandbook.Book;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class BookService {
    private DAO dao = new DAO();

    public ResponseEntity<?> createBooks(String role) throws NullPointerException {
        if (role.equals("admin")) {
            String create = dao.create();
            return ResponseEntity.ok(create);
        } else {
            throw new NullPointerException();
        }
    }

    public ResponseEntity<?> readAllBooks(String role) throws NullPointerException {
        if (role.equals("admin")) {
            List<Book> read = dao.readAll();
            System.out.println("Successfully read all book!");
            return ResponseEntity.ok(read);
        } else {
            throw new NullPointerException();
        }
    }

    public ResponseEntity<?> readByTheAuthor(String role, String name, String surname) throws NullPointerException {
        if (role.equals("admin")) {
            List<Book> read = dao.readByAuthor(name, surname);
            System.out.println("Successfully read all the books of the mentioned author!");
            return ResponseEntity.ok(read);
        } else {
            throw new NullPointerException();
        }
    }

    public ResponseEntity<?> updateTheBook(String role, String title) throws NullPointerException {
        if (role.equals("admin")) {
            Book update = dao.update(title);
            return ResponseEntity.ok(update);
        } else {
            throw new NullPointerException();
        }
    }

    public ResponseEntity<?> deleteTheBook(String role, String title) throws NullPointerException {
        if (role.equals("admin")) {
            Book delete = dao.delete(title);
            return ResponseEntity.ok(delete);
        } else {
            throw new NullPointerException();
        }

    }

}
