package it.uniroma2.ispw.decluttify.model;

import it.uniroma2.ispw.decluttify.exception.DecluttifyException;
import it.uniroma2.ispw.decluttify.exception.ModelException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestUser {

    @Test
    void testCheckPasswordCorrect() {
        String psw = "richard";
        String pepper = "shared-secret";
        User user = new User("richard", psw, 5.0, "richard@test.com", pepper);
        boolean result = user.checkPassword(psw, pepper);
        assertTrue(result);
    }

    @Test
    void testCheckPasswordWrong() {
        String psw = "rightPSW";
        String pepper = "shared-secret";
        User user = new User("richard", "wrongPSW", 5.0, "richard@test.com", pepper);
        boolean result = user.checkPassword(psw, pepper);
        assertFalse(result);
    }

    @Test
    void testCheckPasswordEmpty() {
        String psw = "";
        String pepper = "shared-secret";
        User user = new User("richard", psw, 5.0, "richard@test.com", pepper);
        boolean result = user.checkPassword(psw, pepper);
        assertFalse(result);
    }

    @Test
    void testCheckPasswordNull() {
        String psw = "pwd";
        String pepper = "shared-secret";
        User user = new User("richard", psw, 5.0, "richard@test.com", pepper);
        assertThrows(DecluttifyException.class, () ->{ user.checkPassword(null, pepper);});
    }

    @Test
    void testSetUsernameNullThrows() {
        User user = new User("Mario", "psw", 5.0, "mario@test.com", "pepper");
        assertThrows(ModelException.class, () -> {
            user.setUsername(null);
        });
    }

    @Test
    void testSetPasswordHashNullThrows() {
        User user = new User("Mario", "psw", 5.0, "mario@test.com", "pepper");
        assertThrows(ModelException.class, () -> {
            user.setPasswordHash(null, "pepper");
        });
    }

}
