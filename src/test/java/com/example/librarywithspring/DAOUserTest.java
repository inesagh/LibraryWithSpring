package com.example.librarywithspring;

import com.example.librarywithspring.userandbook.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class DAOUserTest {
    private DAO classUnderTest = new DAO();

    @Test
    @DisplayName("Register with invalid values")
    void registerWithInvalidValues() {
        Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.registerIntoDB(new Person("asd", "asd", "Asd", "saaa", "aaaa", "ASD")));
    }

    @Test
    @DisplayName("Login with invalid values")
    void loginWithInvalidValues() {
        Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.login("asd", ""));
    }

    @Test
    @DisplayName("Choose book with invalid title")
    void choosBookWithInvalidTitle() {
        Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.book(null, "asd"));
    }

}
