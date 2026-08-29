package it.uniroma2.ispw.decluttify.controller.logic;

import it.uniroma2.ispw.decluttify.exception.LoginException;
import it.uniroma2.ispw.decluttify.utils.SessionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestLoginController {

    LoginController loginController;
    SessionManager sessionManager;

    @BeforeEach
    public void setUp() {
        SessionManager sessionManager = new SessionManager();
        this.sessionManager = sessionManager;
        LoginController loginController = new LoginController(sessionManager);
        this.loginController = loginController;
    }

    @Test
    public void testLoginCorrect(){
        boolean result = loginController.login("dave", "dave");
        assertTrue(result);
    }

    @Test
    public void testLoginIncorrect(){
        boolean result = loginController.login("dave", "wrong");
        assertFalse(result);
    }

    @Test
    public void testLoginUsernameNull(){
        assertThrows(LoginException.class, () -> {loginController.login(null, "dave");});
    }

    @Test
    public void testLoginPasswordNull(){
        assertThrows(LoginException.class, () -> {loginController.login("dave", null);});
    }

    @Test
    public void testLoginPasswordEmpty(){
        assertThrows(LoginException.class, () -> {loginController.login("dave", "");});
    }

    @Test
    public void testLoginUsernameEmpty(){
        assertThrows(LoginException.class, () -> {loginController.login("", "password");});
    }

    @Test
    public void testLoginTooManyFailedAttempts(){
        loginController.login("dave", "wrongpwd");
        loginController.login("dave", "wrongpwd");
        loginController.login("dave", "wrongpwd");
        assertThrows(LoginException.class, () -> {loginController.login("dave", "dave");});
    }

    @Test
    public void testLogoutIfLoggedIn(){
        loginController.login("dave", "dave");
        loginController.logout();
        assertFalse(sessionManager.isLoggedIn());
    }

}
