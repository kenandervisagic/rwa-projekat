package org.quiz.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class UsersDAO {

    private static final Logger log = LoggerFactory.getLogger(UsersDAO.class);

    private static final String DB_URL ="jdbc:mysql://localhost:3305/rwa_quiz";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "keno";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            log.error("No JDBC driver loaded {}", e.getMessage());
        }
    }

    public static boolean insertUser(String name, String username, String password, String role) {
        log.info("Attempting to insert a new user: username={}, role={}", username, role);

        String insertSQL = "INSERT INTO users (name, username, password, role) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement(insertSQL)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, role);

            int rowsAffected = preparedStatement.executeUpdate();
            log.debug("Executing update: {}", insertSQL);

            if (rowsAffected > 0) {
                log.info("A new user was inserted successfully: username={}", username);
                return true;
            } else {
                log.warn("No rows affected, user insertion failed: username={}", username);
                return false;
            }
        } catch (SQLException e) {
            log.error("Error inserting user: username={}", username, e);
            return false;
        }
    }

    public static String getHashedPassword(String username) {
        String query = "SELECT password FROM users WHERE username = ?";
        String hashedPassword = null;

        log.info("Attempting to retrieve hashed password for username={}", username);

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            log.debug("Executing query: {}", query);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    hashedPassword = resultSet.getString("password");
                    log.info("Successfully retrieved hashed password for username={}", username);
                } else {
                    log.warn("No hashed password found for username={}", username);
                }
            }

        } catch (SQLException e) {
            log.error("SQL error while retrieving hashed password for username={}: {}", username, e.getMessage(), e);
        }

        return hashedPassword;
    }

}
