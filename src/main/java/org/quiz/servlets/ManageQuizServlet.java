package org.quiz.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet("/admin/createQuiz")
public class ManageQuizServlet extends HttpServlet {

    // Logger
    private static final Logger log = LoggerFactory.getLogger(ManageQuizServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("manageQuiz.jsp").forward(request, response);
        log.info("manageQuiz.jsp GET request served");
    }
}
