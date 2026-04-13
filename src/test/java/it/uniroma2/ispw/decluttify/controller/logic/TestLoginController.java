package it.uniroma2.ispw.decluttify.controller.logic;

import it.uniroma2.ispw.decluttify.exception.ModelException;
import it.uniroma2.ispw.decluttify.utils.SessionManager;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestLoginController {

    @Test
    public void testLoginCorrect(){
        LoginController loginController = new LoginController();
        boolean result = loginController.login("dave", "dave");
        assertTrue(result);
    }

    @Test
    public void testLoginIncorrect(){
        LoginController loginController = new LoginController();
        boolean result = loginController.login("dave", "wrong");
        assertFalse(result);
    }

    @Test
    public void testLoginUsernameNull(){
        LoginController loginController = new LoginController();
        boolean result = loginController.login(null, "dave");
        assertFalse(result);
    }

    @Test
    public void testLoginPasswordNull(){
        LoginController loginController = new LoginController();
        assertThrows(ModelException.class, () -> {loginController.login("dave", null);});
    }

    @Test
    public void testLoginPasswordEmpty(){
        LoginController loginController = new LoginController();
        assertThrows(ModelException.class, () -> {loginController.login("dave", "");});
    }

    @Test
    public void testLoginUsernameEmpty(){
        LoginController loginController = new LoginController();
        boolean result = loginController.login("", "pwd");
        assertFalse(result);
    }

    @Test
    public void testLogoutIfLoggedIn(){
        LoginController loginController = new LoginController();
        loginController.login("dave", "dave");
        loginController.logout();
        assertFalse(SessionManager.getInstance().isLoggedIn());
    }

    @Test
    public void testLogoutIfLoggedOut(){
        LoginController loginController = new LoginController();
        loginController.logout();
        assertFalse(SessionManager.getInstance().isLoggedIn());
    }

}
