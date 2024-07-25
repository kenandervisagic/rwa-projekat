package org.quiz.users;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.quiz.database.UsersDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;

@WebServlet("/deleteUser")
public class DeleteUserServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(DeleteUserServlet.class);
    private final Gson gson = new Gson(); // Gson object for JSON parsing

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Parse JSON request body using Gson
        User user = gson.fromJson(new InputStreamReader(request.getInputStream()), User.class);
        String userId = user.getId();

        if (userId == null || userId.isEmpty()) {
            log.warn("User ID is missing in the delete request");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"message\": \"User ID is required\"}");
            return;
        }

        try {
            // Call UsersDAO to delete the user
            boolean success = UsersDAO.deleteUser(userId);

            if (success) {
                log.info("Successfully deleted user with ID={}", userId);
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"message\": \"User deleted successfully\"}");
            } else {
                log.warn("Failed to delete user with ID={}", userId);
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"message\": \"User not found\"}");
            }
        } catch (Exception e) {
            log.error("Error deleting user with ID={}", userId, e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"message\": \"An error occurred while deleting the user\"}");
        }
    }

    // Inner class to match the JSON structure
    public static class User {
        private String id;

        // Getter and setter for id
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

}
