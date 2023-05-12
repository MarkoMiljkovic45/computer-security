import db.Database;
import db.DatabaseEntry;
import util.User;
import util.Util;

import java.io.Console;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

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
        DatabaseEntry entry = Database.getEntry(username);
        Console console = System.console();
        boolean match = false;

        while(!match) {
            String password = new String(console.readPassword("Password: "));

            if (entry != null) {
                match = verifyPassword(password, entry.getSalt(), entry.getPasswordHash());
            }

            if (!match) {
                System.out.println("Username or password incorrect.");
            }
        }

        if (entry.isForcepass()) {
            boolean changeSuccessful = forcepass(entry);

            if (!changeSuccessful) {
                return;
            }
        }

        System.out.println("Login successful.");
    }

    private static boolean forcepass(DatabaseEntry entry) throws IOException {
        Console console = System.console();
        String password = new String(console.readPassword("New password: "));

        if (password.length() < 8) {
            System.out.println("Password change failed. Password must be at least 8 characters long.");
            return false;
        }

        if (verifyPassword(password, entry.getSalt(), entry.getPasswordHash())) {
            System.out.println("Password change failed. Password mustn't match old password.");
            return false;
        }

        String repeatPassword = new String(console.readPassword("Repeat new password: "));

        if (!password.equals(repeatPassword)) {
            System.out.println("Password change failed. Password mismatch.");
            return false;
        }

        Database.deleteEntry(entry.getUsername());
        Database.addEntry(new User(entry.getUsername(), password));

        return true;
    }

    private static boolean verifyPassword(String password, byte[] salt, byte[] checkHash) {
        byte[] saltedPassword = Util.toByteArray(password.getBytes(StandardCharsets.UTF_8), salt);
        byte[] passwordHash = Util.hashMessage(saltedPassword);
        return Arrays.equals(checkHash, passwordHash);
    }
}
