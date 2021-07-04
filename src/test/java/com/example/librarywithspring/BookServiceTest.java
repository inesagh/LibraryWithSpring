package com.example.librarywithspring;

import com.example.librarywithspring.service.BookService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BookServiceTest {
    private BookService classUnderTest = new BookService();

    @Test
    @DisplayName("Creating by user")
    void userCreatesBooks() {
        Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.createBooks("user"));
    }

    @Test
    @DisplayName("Reading all books by user")
    void userReadsBooks() {
        Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.readAllBooks("user"));
    }

    @Test
    @DisplayName("Reading the book by user")
    void userReadsTheBooks() {
        Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.readByTheAuthor("user", "", ""));
    }

    @Test
    @DisplayName("Update by user")
    void userUpdatesBook() {
        Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.updateTheBook("user", ""));
    }

    @Test
    @DisplayName("Delete by user")
    void userDeletesTheBook() {
        Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.deleteTheBook("user", ""));
    }

}
