package com.example.librarywithspring;

import com.example.librarywithspring.userandbook.Person;
import com.example.librarywithspring.userandbook.UserWithoutPassword;

public class UserChanging {
    private UserWithoutPassword userWithoutPassword;

    public UserWithoutPassword hidingUserInfo(Person person) {
        userWithoutPassword = new UserWithoutPassword(
                person.getUsername(),
                person.getName(),
                person.getSurname(),
                person.getEmail(),
                person.getRole()
        );
        return userWithoutPassword;
    }
}
