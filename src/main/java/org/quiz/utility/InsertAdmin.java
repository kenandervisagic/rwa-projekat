package org.quiz.utility;

import org.quiz.database.UsersDAO;
import org.quiz.password.PasswordUtils;

public class InsertAdmin {
    public static void main(String[] args) {
        UsersDAO.insertUser("admin", "admin", PasswordUtils.hashPassword("admin"), "admin");
    }
}
