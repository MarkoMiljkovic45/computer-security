package hr.fer.srs;

import hr.fer.srs.commands.ShellCommand;
import hr.fer.srs.env.Environment;
import hr.fer.srs.env.ShellEnvironment;
import hr.fer.srs.env.ShellStatus;
import hr.fer.srs.util.reader.StringReader;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Password Manager Application
 * <p>
 * It works like a console that supports the following commands:
 * <ul>
 *     <li>init [MASTER]         - Initializes the master password, deletes saved passwords if present</li>
 *     <li>get  [URL]            - Returns the password for a given web address</li>
 *     <li>put  [URL] [PASSWORD] - Sets a password for a given web address</li>
 *     <li>help [COMMAND]        - Prints the manual for a certain command</li>
 *     <li>exit                  - Terminates the shell</li>
 * </ul>
 */
public class PasswordManager {
    public static final Charset CHARSET      = StandardCharsets.UTF_8;
    public static final int PASSWORD_LENGTH  = 256;
    public static final int PASSWORD_PADDING = 16;

    public static void main(String[] args) {
        System.out.println("Welcome to Password Manager v1.0");

        ShellStatus status = ShellStatus.CONTINUE;

        Environment env = new ShellEnvironment();
        initEnvironment(env);

        while (status != ShellStatus.TERMINATE) {
            env.write(env.getPromptSymbol() + " ");
            String userInput = env.readLine();
            status = executeCommand(env, userInput);
        }
    }

    private static ShellStatus executeCommand(Environment env, String userInput) {
        if (userInput.equals("")) {
            return ShellStatus.CONTINUE;
        }

        StringReader reader = new StringReader(userInput);

        String commandName = reader.read(c -> c == ' ');
        reader.skip(1);                                         //Skip whitespace ' '

        String arguments = "";

        if (reader.hasNext()) {
            arguments = reader.read();
        }

        try {
            ShellCommand command = env.commands().get(commandName);

            if (command == null) {
                env.writeln("Command " + commandName + " doesn't exist.");
            } else {
                return command.executeCommand(env, arguments);
            }
        } catch (Exception e) {
            env.writeln(e.getMessage());
        }

        return ShellStatus.CONTINUE;
    }

    private static void initEnvironment(Environment env) {
        try {
            env.initSecretKeys();
        } catch (Exception e) {
            System.out.println(e.getMessage());

            System.out.print("\nDo you want to initialize a new master password? WARNING: This will delete the previously stored passwords! Y/N: ");

            Scanner sc = new Scanner(System.in);
            String choice = sc.nextLine().toLowerCase();

            if (choice.equals("y")) {
                System.out.print("Enter new master password: ");
                String newMasterPassword = sc.nextLine();
                env.setMasterPassword(newMasterPassword);
            } else {
                System.exit(0);
            }
        }
    }
}
