package com.example.librarywithspring;

import com.example.librarywithspring.userandbook.Person;
import com.example.librarywithspring.userandbook.UserWithoutPassword;
import com.github.javafaker.Faker;
import org.mindrot.jbcrypt.BCrypt;
import com.example.librarywithspring.userandbook.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DAO {
    static String DB_PATH = "jdbc:mariadb://localhost:3306/user_db";
    static String USER = "root";
    static String PASS = "chemasi";
    Person user = null;
    int idOfLoggedIn = 0;
    Faker faker = new Faker();
    Scanner scanner = new Scanner(System.in);

    public DAO() {
        createDB();
    }

    public void createDB() {
        Connection connection = connection();
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE DATABASE IF NOT EXISTS user_db");
            statement.execute("use user_db");
            statement.execute("CREATE TABLE IF NOT EXISTS person(id INT NOT NULL UNIQUE AUTO_INCREMENT PRIMARY KEY, name VARCHAR (20), surname VARCHAR (20), email VARCHAR (20), username VARCHAR (15) NOT NULL UNIQUE , password VARCHAR (255) UNIQUE NOT NULL, role VARCHAR(10) NOT NULL)");

            statement.execute("CREATE TABLE IF NOT EXISTS author(id INT NOT NULL UNIQUE AUTO_INCREMENT PRIMARY KEY, name VARCHAR (20), surname VARCHAR (30), age INT NOT NULL, number_of_books INT)");
            statement.execute("CREATE TABLE IF NOT EXISTS book(id INT NOT NULL UNIQUE AUTO_INCREMENT PRIMARY KEY, title VARCHAR(100), published_year VARCHAR(10) , status ENUM('TAKEN', 'NOT_TAKEN'))");
            statement.execute("CREATE TABLE IF NOT EXISTS book_author(book_id INT UNIQUE REFERENCES book(id), author_id INT REFERENCES author(id))");
            statement.execute("CREATE TABLE IF NOT EXISTS user_book(user_id INT REFERENCES person(id), book_id INT UNIQUE REFERENCES book(id), start_day DATE , end_day DATE)");


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public Connection connection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DB_PATH, USER, PASS);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return connection;
    }

    public Person registerIntoDB(Person person) {
        Connection connection = connection();
        if (!person.getEmail().contains("@")) {
            throw new NullPointerException();
        } else {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO person(name, surname, email, username, password, role) VALUES (?, ?, ?, ?, ?, ?)");
                preparedStatement.setString(1, person.getName());
                preparedStatement.setString(2, person.getSurname());
                preparedStatement.setString(3, person.getEmail());
                preparedStatement.setString(4, person.getUsername());
                preparedStatement.setString(5, person.getPassword());
                preparedStatement.setString(6, person.getRole());

                ResultSet resultSet = preparedStatement.executeQuery();

                PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT id FROM person WHERE password = ?");
                preparedStatement1.setString(1, person.getPassword());
                ResultSet resultSet1 = preparedStatement1.executeQuery();
                while (resultSet1.next()) {
                    idOfLoggedIn = resultSet1.getInt("id");
                }

                user.setName(person.getName());
                user.setSurname(person.getSurname());
                user.setUsername(person.getUsername());
                user.setEmail(person.getEmail());
                user.setPassword(person.getPassword());
                user.setRole(person.getRole());

                System.out.println("Succeed!");
                return user;

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return user;
        }
    }

    public Person login(String username, String password) {
        String message = "";
        Connection connection = connection();
        try {
            String command0 = "SELECT password FROM person WHERE username = ?";
            PreparedStatement preparedStatement1 = connection.prepareStatement(command0);
            preparedStatement1.setString(1, username);
            ResultSet resultSet1 = preparedStatement1.executeQuery();
            String hashedPassword = null;
            while (resultSet1.next()) {
                hashedPassword = resultSet1.getString("password");
            }
            if (BCrypt.checkpw(password, hashedPassword)) {
                String command = "SELECT * FROM person WHERE username = ? AND password = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(command);
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, hashedPassword);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (!resultSet.next()) {
                    message = "There is not such an account. Please, register first.";
                    throw new NullPointerException();
                } else {
                    idOfLoggedIn = resultSet.getInt("id");
                    user = new Person(resultSet.getString("name"),
                            resultSet.getString("surname"),
                            resultSet.getString("email"),
                            resultSet.getString("username"),
                            resultSet.getString("password"),
                            resultSet.getString("role")
                    );
                    String fullName = user.getName() + " " + user.getSurname();
                    message = "Successfully login: " + fullName;
                }
            }
            System.out.println(message);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return user;
    }

    public String book(UserWithoutPassword userWithoutPassword, String title) {
        Connection connection = connection();
        try {
            String command = "SELECT id FROM book WHERE LOWER(REPLACE(title, ' ', '')) = LOWER(REPLACE(?, ' ', '')) AND status = 'NOT_TAKEN'";
            PreparedStatement preparedStatement = connection.prepareStatement(command);
            preparedStatement.setString(1, title);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int idForBook = resultSet.getInt("id");

                PreparedStatement bookWithChangedStatus = connection.prepareStatement("UPDATE book SET status = 'TAKEN' WHERE id = ?");
                bookWithChangedStatus.setInt(1, idForBook);
                bookWithChangedStatus.executeQuery();

                PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT id FROM person WHERE username = ?");
                preparedStatement1.setString(1, userWithoutPassword.getUsername());
                ResultSet resultSet1 = preparedStatement1.executeQuery();
                if (resultSet1.next()) {
                    idOfLoggedIn = resultSet1.getInt("id");
                } else {
                    System.out.println("Invalid user");
                    throw new NullPointerException();
                }
                PreparedStatement insertIntoUserBook = connection.prepareStatement("INSERT INTO user_book(user_id, book_id, start_day, end_day) VALUES (?, ?, NOW(), DATE_ADD(NOW(), INTERVAL 15 DAY))");
                insertIntoUserBook.setInt(1, idOfLoggedIn);
                insertIntoUserBook.setInt(2, idForBook);
                insertIntoUserBook.executeQuery();

                return ("Thank you:) You have to return the book within 15 days.");
            } else {
                System.out.println("We haven't this book at this moment :(");
                throw new NullPointerException();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return "";
    }


//    CRUD

    public String create() {
        for (int i = 0; i < 10; i++) {

            String title = faker.book().title();
            String publishedYear = faker.random().nextInt(1900, 2021) + "";
            String[] author = faker.book().author().split(" ");
            String name = author[0];
            String surname = author[1];
            Integer age = faker.random().nextInt(20, 100);
            Integer numberOfBooks = faker.random().nextInt(1, 80);

            Connection connection = connection();
            try {
                PreparedStatement book = connection.prepareStatement("INSERT INTO book(title, published_year, status) VALUES (?, ?, 'NOT_TAKEN')");
                book.setString(1, title);
                book.setString(2, publishedYear);
                book.executeQuery();

                PreparedStatement authorPrepSt = connection.prepareStatement("INSERT INTO author(name, surname, age, number_of_books) VALUES (?, ?, ?, ?)");
                authorPrepSt.setString(1, name);
                authorPrepSt.setString(2, surname);
                authorPrepSt.setInt(3, age);
                authorPrepSt.setInt(4, numberOfBooks);
                authorPrepSt.executeQuery();

                PreparedStatement bookIdPrepSt = connection.prepareStatement("SELECT id FROM book WHERE title = ? AND published_year = ?");
                bookIdPrepSt.setString(1, title);
                bookIdPrepSt.setString(2, publishedYear);
                ResultSet resultSet = bookIdPrepSt.executeQuery();
                int bookId = 0;
                while (resultSet.next()) {
                    bookId = resultSet.getInt("id");
                }

                PreparedStatement authorIdPrepSt = connection.prepareStatement("SELECT id FROM author WHERE name = ? AND surname = ?");
                authorIdPrepSt.setString(1, name);
                authorIdPrepSt.setString(2, surname);
                ResultSet resultSet1 = authorIdPrepSt.executeQuery();
                int authorId = 0;
                while (resultSet1.next()) {
                    authorId = resultSet1.getInt("id");
                }

                PreparedStatement bookAuthor = connection.prepareStatement("INSERT INTO book_author(book_id, author_id) VALUES (?, ?)");
                bookAuthor.setInt(1, bookId);
                bookAuthor.setInt(2, authorId);
                bookAuthor.executeQuery();


            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return "Successfully created 10 more books!";

    }

    public List<Book> readAll() {
        List<Book> books = new ArrayList<>();
        Connection connection = connection();
        try {
            Statement statement = connection.createStatement();
            String command = "SELECT * FROM book";
            ResultSet resultSet = statement.executeQuery(command);
            while (resultSet.next()) {
                Book book = new Book(resultSet.getString("title"),
                        resultSet.getString("published_year"));
                books.add(book);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return books;
    }

    public List<Book> readByAuthor(String name, String surname) {
        Connection connection = connection();
        List<Book> books = new ArrayList<>();

        try {
            String command = "SELECT DISTINCT title, published_year FROM author, book, book_author WHERE book_id = book.id AND author_id = author.id AND LOWER(REPLACE(CONCAT(name, surname), ' ', '')) = LOWER(REPLACE(?, ' ', ''))";
            PreparedStatement preparedStatement = connection.prepareStatement(command);
            preparedStatement.setString(1, name + surname);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Book book = new Book(resultSet.getString("title"),
                        resultSet.getString("published_year"));
                books.add(book);
            }
            if (books.size() == 0) {
                System.out.println("If your SYNTAX is VALID then now we haven't a book of this author. Please try another day.");
                throw new NullPointerException();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return books;
    }

    public Book update(String title) {
        Connection connection = connection();
        Book book = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM book WHERE LOWER(REPLACE(title, ' ', '')) = LOWER(REPLACE(?, ' ', '')) AND status = 'NOT_TAKEN' LIMIT 1");
            preparedStatement.setString(1, title);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                System.out.println("There isn't such a book that is not taken. Please check your syntax.");
                throw new NullPointerException();
            } else {

                int idForUpdate = resultSet.getInt("id");

                PreparedStatement preparedStatement1 = connection.prepareStatement("UPDATE book SET status = 'TAKEN' WHERE id = ?");
                preparedStatement1.setInt(1, idForUpdate);
                ResultSet resultSet1 = preparedStatement1.executeQuery();

                PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT * FROM book WHERE id = ?");
                preparedStatement2.setInt(1, idForUpdate);
                ResultSet resultSet2 = preparedStatement2.executeQuery();
                if (resultSet2.next()) {
                    book = new Book(resultSet2.getString("title"),
                            resultSet2.getString("published_year"));
                }

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return book;

    }

    public Book delete(String title) {
        Connection connection = connection();
        Book book = null;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM book WHERE LOWER(REPLACE(title, ' ', '')) = LOWER(REPLACE(?, ' ', '')) AND status = 'NOT_TAKEN' LIMIT 1");
            preparedStatement.setString(1, title);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                System.out.println("There isn't such a book. Please check your syntax.");
                throw new NullPointerException();
            } else {
                int idForDelete = resultSet.getInt("id");

                PreparedStatement preparedStatement3 = connection.prepareStatement("SELECT * FROM book WHERE id = ?");
                preparedStatement3.setInt(1, idForDelete);
                ResultSet resultSet3 = preparedStatement3.executeQuery();
                if (resultSet3.next()) {
                    book = new Book(resultSet3.getString("title"),
                            resultSet3.getString("published_year"));
                }

                System.out.println(idForDelete);
                PreparedStatement preparedStatement1 = connection.prepareStatement("DELETE FROM book_author WHERE book_id = ?");
                preparedStatement1.setInt(1, idForDelete);
                ResultSet resultSet1 = preparedStatement1.executeQuery();
                PreparedStatement preparedStatement2 = connection.prepareStatement("DELETE FROM book WHERE id = ?");
                preparedStatement2.setInt(1, idForDelete);
                ResultSet resultSet2 = preparedStatement2.executeQuery();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return book;
    }
}
