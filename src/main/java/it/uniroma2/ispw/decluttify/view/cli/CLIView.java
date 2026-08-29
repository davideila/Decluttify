package it.uniroma2.ispw.decluttify.view.cli;

import it.uniroma2.ispw.decluttify.utils.SessionManager;
import java.util.Scanner;

public abstract class CLIView {
    protected final Scanner scanner = new Scanner(System.in);
    protected final SessionManager sessionManager;

    public CLIView(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void show(){
        this.showMenu();
        this.showFunctions();
    }

    public void showMenu(){
        if(sessionManager.isLoggedIn()){
            System.out.println("Logged in as: " + sessionManager.getLoggedUser().getUsername());
            System.out.println("[p] My Profile    | [l] Log Out       | [h] Home          | [b] Back          ");

        }
        else {
            System.out.println("[l] Log In        | [r] Register      | [h] Home          | [b] Back          ");
        }
        System.out.println("[f] Item Browser  | [i] My Items      | [o] My Offers     | [m] My Barters    ");
        System.out.println("[d] Donate        | [e] Exit          ");
    }

    public abstract void showFunctions();

    protected void printHeader(String title) {
        clearConsole();
        System.out.println("\n========================================");
        System.out.println("   " + title.toUpperCase());
        System.out.println("========================================");
    }

    public String getInput(String prompt) {
        System.out.print(prompt);
        return this.scanner.nextLine().toUpperCase();
    }

    protected void clearConsole() {
        for (int i = 0; i < 50; ++i) System.out.println();
    }

    public void showMessage(String msg, boolean err){
        if (err) {
            System.err.println(msg);
        }
        else{
            System.out.println(msg);
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    public Scanner getScanner() {
        return scanner;
    }
}
