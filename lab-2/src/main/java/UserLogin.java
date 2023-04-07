import db.Database;
import db.DatabaseEntry;
import util.User;

import java.io.Console;
import java.io.IOException;

public class UserLogin {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Invalid number of arguments.");
            return;
        }

        try {
            String username  = args[0];
            login(username);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void login(String username) throws IOException {
        //TODO Adapt for login use
        DatabaseEntry entry = Database.getEntry(username);
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
}
