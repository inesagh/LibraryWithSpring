package com.example.librarywithspring.service;

import com.example.librarywithspring.DAO;
import com.example.librarywithspring.UserChanging;
import com.example.librarywithspring.userandbook.Person;
import com.example.librarywithspring.userandbook.UserWithoutPassword;
import org.springframework.http.ResponseEntity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService {
    private DAO dao = new DAO();

    public UserWithoutPassword registration(Person person) {
        if (person != null) {
            Person registeredPerson = dao.registerIntoDB(person);
            UserChanging userChanging = new UserChanging();
            UserWithoutPassword userWithoutPassword = userChanging.hidingUserInfo(registeredPerson);
            return userWithoutPassword;
        } else {
            throw new NullPointerException();
        }
    }

    public ResponseEntity<Boolean> login(String username, String password) {
        Person loggedinPerson = null;
        loggedinPerson = dao.login(username, password);
        UserChanging userChanging = new UserChanging();
        UserWithoutPassword userWithoutPassword = userChanging.hidingUserInfo(loggedinPerson);
        if (userWithoutPassword != null) {
            return ResponseEntity.ok(true);
        } else {
            throw new NullPointerException();
        }
    }

    public List<Map<String, String>> currentAvailableBooks() {
        Connection connection = dao.connection();
        List<Map<String, String>> currentBooks = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            String command = "SELECT title, name, surname FROM author, book, book_author WHERE book.id = book_author.book_id AND author.id = book_author.author_id AND status = 'NOT_TAKEN';";
            ResultSet resultSet = statement.executeQuery(command);
            while (resultSet.next()) {
                Map<String, String> currentBooksMap = new HashMap<>();
                String author = resultSet.getString("name") + " " +
                        resultSet.getString("surname");
                String title = resultSet.getString("title");
                currentBooksMap.put("author", author);
                currentBooksMap.put("title", title);
                currentBooks.add(currentBooksMap);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return currentBooks;
    }

    public ResponseEntity<?> choosenBook(String username, String name, String surname, String email, String role, String title) {
        if (username.isEmpty() || name.isEmpty() || surname.isEmpty() || email.isEmpty() || role.isEmpty() || !(role.equals("admin") || role.equals("user"))) {
            throw new NullPointerException();
        } else {
            UserWithoutPassword userFromHeader = new UserWithoutPassword(username, name, surname, email, role);
            String book = dao.book(userFromHeader, title);
            return ResponseEntity.ok(book);
        }
    }
}
