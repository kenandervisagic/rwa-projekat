package org.quiz.users;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.quiz.database.UsersDAO;
import org.quiz.password.PasswordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.util.Base64;

@WebServlet("/loginAuth")
public class LoginAuthServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(LoginAuthServlet.class);
    private final Gson gson = new Gson(); // Gson object for JSON parsing

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Parse JSON request body using Gson
        User user = gson.fromJson(new InputStreamReader(request.getInputStream()), User.class);

        log.info("Received login request for username={}", user.getUsername());

        // Retrieve the hashed password from the database (UsersDAO should handle this)
        String hashedPassword = UsersDAO.getHashedPassword(user.getUsername());

        if (hashedPassword != null && PasswordUtils.checkPassword(user.getPassword(), hashedPassword)) {
            log.info("User logged in successfully: username={}", user.getUsername());

            // Generate a secure token
            String token = generateToken();
            log.info("Generated token for user: {}", token);

            // Create a cookie with the token
            Cookie cookie = new Cookie("authToken", token);
            cookie.setHttpOnly(true); // Protect against XSS attacks
            cookie.setSecure(true); // Ensure cookie is sent over HTTPS
            cookie.setMaxAge(60 * 20); // Cookie expires in 20 minutes
            response.addCookie(cookie);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"message\": \"Login successful\", \"token\": \"" + token + "\"}");
        } else {
            log.warn("Failed login attempt: username={}", user.getUsername());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"message\": \"Invalid username or password\"}");
        }
    }

    // Method to generate a secure token
    private String generateToken() {
        SecureRandom random = new SecureRandom();
        byte[] tokenBytes = new byte[24]; // Generate a 24-byte token
        random.nextBytes(tokenBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }

    // User class to match the JSON structure
    public static class User {
        private String username;
        private String password;

        // Getters and setters
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
