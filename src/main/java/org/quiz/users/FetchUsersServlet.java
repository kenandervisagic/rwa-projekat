package org.quiz.users;

import com.google.gson.Gson;
import org.quiz.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.quiz.database.UsersDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/fetchUsers")
public class FetchUsersServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(FetchUsersServlet.class);
    private final Gson gson = new Gson(); // Gson object for JSON parsing

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("Received request to fetch users");

        List<User> users = UsersDAO.getAllUsers();

        if (users.isEmpty()) {
            log.error("Failed to retrieve users from database");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"message\": \"Internal Server Error\"}");
            return;
        }

        String jsonResponse = gson.toJson(users);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);

        log.info("Successfully sent {} users to client", users.size());
    }
}
