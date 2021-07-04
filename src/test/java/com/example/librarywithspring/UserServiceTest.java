package com.example.librarywithspring;

import com.example.librarywithspring.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class UserServiceTest {
    private UserService classUnderTest = new UserService();

    @Test
    @DisplayName("Registration")
    void register() {
        Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.registration(null));
    }

    @Test
    @DisplayName("Login with null")
    void loginWithNull() {
        Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.login(null, null));
    }

    @Test
    @DisplayName("Login with valid values")
    void loginWithValidValues() {
        Assertions.assertEquals(ResponseEntity.ok(true), classUnderTest.login("ines", "asd"));
    }

    @Test
    @DisplayName("Choose book wit invalid values")
    void chooseBookWithInvalidValues() {
        Assertions.assertThrows(NullPointerException.class, () -> classUnderTest.choosenBook("asd", " ", "aaa", "ddd", "aaaa", "afff"));
    }

}
