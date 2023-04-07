import db.Database;
import util.User;

import java.io.Console;
import java.io.IOException;

public class UserManagement {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Invalid number of arguments.");
            return;
        }

        String command  = args[0];
        String username = args[1];

        try {
            switch (command) {
                case "add"       -> add(username);
                case "passwd"    -> passwd(username);
                case "forcepass" -> forcepass(username);
                case "del"       -> del(username);
                default          -> System.out.printf("Command %s not recognised\n", command);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 1. Check if username in use
     * 2. Prompt for password
     * 3. Check il len > 8
     * 4. Retype password and check for match
     * 5. Salt and save the password
     * @param username Username for the new user
     */
    private static void add(String username) throws IOException {
        if (Database.containsEntry(username)) {
            System.out.println("Username already in use.");
            return;
        }

        Console console = System.console();
        String password = new String(console.readPassword("Password: "));

        if (password.length() < 8) {
            System.out.println("User add failed. Password must be at least 8 characters long.");
            return;
        }

        String repeatPassword = new String(console.readPassword("Repeat Password: "));

        if (!password.equals(repeatPassword)) {
            System.out.println("User add failed. Password mismatch.");
            return;
        }

        Database.addEntry(new User(username, password));
        System.out.printf("User %s successfully added.\n", username);
    }

    /**
     * 1. Check if username exists
     * 2. Prompt for password
     * 3. Check il len > 8
     * 4. Retype password and check for match
     * 5. Salt and save the password
     * @param username Username of user whose password we are changing
     */
    private static void passwd(String username) throws IOException {
        if (!Database.containsEntry(username)) {
            System.out.println("Username doesn't exist.");
            return;
        }

        Console console = System.console();
        String password = new String(console.readPassword("Password: "));

        if (password.length() < 8) {
            System.out.println("Password change failed. Password must be at least 8 characters long.");
            return;
        }

        String repeatPassword = new String(console.readPassword("Repeat Password: "));

        if (!password.equals(repeatPassword)) {
            System.out.println("Password change failed. Password mismatch.");
            return;
        }

        Database.deleteEntry(username);
        Database.addEntry(new User(username, password));

        System.out.println("Password change successful.");
    }

    /**
     * 1. Check if user exits
     * 2. Set forcepass to true
     * @param username Username for user who will be forced to change the password
     */
    private static void forcepass(String username) throws IOException {
        if (!Database.containsEntry(username)) {
            System.out.println("Username doesn't exist.");
            return;
        }

        Database.setForcepass(username, true);
        System.out.println("User will be requested to change password on next login.");
    }

    /**
     * 1. Check if user exits
     * 2. Delete user
     * @param username Username for user who will be deleted
     */
    private static void del(String username) throws IOException {
        if (!Database.containsEntry(username)) {
            System.out.println("Username doesn't exist.");
            return;
        }

        Database.deleteEntry(username);
        System.out.println("User successfully removed.");
    }
}
