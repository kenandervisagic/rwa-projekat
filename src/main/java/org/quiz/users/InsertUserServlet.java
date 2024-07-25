package org.quiz.users;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.quiz.database.UsersDAO;
import org.quiz.entity.User;
import org.quiz.password.PasswordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;

@WebServlet("/insertUser")
public class InsertUserServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(InsertUserServlet.class);

    private final Gson gson = new Gson(); // Gson object for JSON parsing

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Parse JSON request body using Gson
        User user = gson.fromJson(new InputStreamReader(request.getInputStream()), User.class);

        log.info("Received request to insert user: username={}, role={}", user.getUsername(), user.getRole());

        // Hash the password before inserting it into the database
        String hashedPassword = PasswordUtils.hashPassword(user.getPassword());

        // Call the UsersDAO to insert the user
        boolean success = UsersDAO.insertUser(user.getName(), user.getUsername(), hashedPassword, user.getRole());

        if (success) {
            log.info("User inserted successfully: username={}", user.getUsername());
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"message\": \"User inserted successfully\"}");
        } else {
            log.warn("Failed to insert user: username={}", user.getUsername());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"message\": \"Failed to insert user\"}");
        }
    }
}
