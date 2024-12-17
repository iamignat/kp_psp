package ignat.malko.util;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

import java.nio.charset.StandardCharsets;

public class PasswordEncryptor {
    public static String hashPassword(String password) {
        Argon2 argon = Argon2Factory.create();
        try {
            return argon.hash(10, 65536, 1, password.getBytes(StandardCharsets.UTF_8));
        } finally {
            argon.wipeArray(password.toCharArray());
        }
    }
    public static boolean verify(String password, String hashPassword) {
        Argon2 argon = Argon2Factory.create();
        return argon.verify(hashPassword, password.getBytes(StandardCharsets.UTF_8));
    }
}
