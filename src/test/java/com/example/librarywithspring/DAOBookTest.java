package com.example.librarywithspring;

import com.example.librarywithspring.userandbook.Book;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.*;
import java.util.List;

@SpringBootTest
public class DAOBookTest {
    private DAO classUnderTest = new DAO();

    @Test
    @DisplayName("Create 10 more books by admin")
    void createBooks() {
        int number = 0, numberTest = 0;
        try (Connection connection = DriverManager.getConnection(DAO.DB_PATH, DAO.USER, DAO.PASS)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("Select COUNT(id) AS total_books FROM book");
            if (resultSet.next()) {
                number = resultSet.getInt("total_books");
            }
            classUnderTest.create();
            ResultSet resultSetTest = statement.executeQuery("Select COUNT(id) AS total_books FROM book");
            if (resultSetTest.next()) {
                numberTest = resultSetTest.getInt("total_books");
            }
            number += 10;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Assertions.assertEquals(number, numberTest);
    }

    @Test
    @DisplayName("Read all books by admin")
    void readBooks() {
        int number = 0, numberTest = 0;
        numberTest = classUnderTest.readAll().size();
        try (Connection connection = DriverManager.getConnection(DAO.DB_PATH, DAO.USER, DAO.PASS)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(id) AS read_books FROM book");
            if (resultSet.next()) {
                number = resultSet.getInt("read_books");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Assertions.assertEquals(number, numberTest);
    }

    @Test
    @DisplayName("Read the book by admin")
    void readTheBook() {
        List<Book> books = classUnderTest.readByAuthor("robin", "sharma");
        Assertions.assertEquals(books.size(), 2);
    }

    @Test
    @DisplayName("Update the book by admin")
    void updateTheBook() {
        String title = "Mother Night";
        String status = "";
        Book book = classUnderTest.update(title);
        try (Connection connection = DriverManager.getConnection(DAO.DB_PATH, DAO.USER, DAO.PASS)) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT status FROM book WHERE title = ? LIMIT 1");
            preparedStatement.setString(1, title);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                status = resultSet.getString("status");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        Assertions.assertEquals(status, "TAKEN");
    }

    @Test
    @DisplayName("Delete the invalid book by admin")
    void deleteTheInvalidBook() {
        String title = "Shall not Perish";

        Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.delete(title));
    }

    @Test
    @DisplayName("Delete the book by admin")
    void deleteTheBook() {
        String title = "Wildfire at Midnight";
        int id = 0;
        boolean isDeleted = false;
        try (Connection connection = DriverManager.getConnection(DAO.DB_PATH, DAO.USER, DAO.PASS)) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM book WHERE title = ? AND status = 'NOT_TAKEN' LIMIT 1");
            preparedStatement.setString(1, title);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt("id");
            }

            Book book = classUnderTest.delete(title);

            PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT id FROM book WHERE id = ?");
            preparedStatement1.setInt(1, id);
            ResultSet resultSet1 = preparedStatement.executeQuery();
            if (!resultSet1.next()) {
                isDeleted = true;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        Assertions.assertEquals(true, isDeleted);
    }

}
