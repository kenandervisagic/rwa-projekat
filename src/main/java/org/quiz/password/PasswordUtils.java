package org.quiz.password;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    // Hash a password
    public static String hashPassword(String password) {
        // Generate a salt and hash the password
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // Verify a password
    public static boolean checkPassword(String password, String hashed) {
        // Check if the provided password matches the hashed password
        return BCrypt.checkpw(password, hashed);
    }
}
