package hr.fer.srs;

import hr.fer.srs.commands.ShellCommand;
import hr.fer.srs.env.Environment;
import hr.fer.srs.env.ShellEnvironment;
import hr.fer.srs.env.ShellStatus;
import hr.fer.srs.util.StringReader;

/**
 * Password Manager Application
 * <p>
 * It works like a console that supports 4 commands:
 * <ul>
 *     <li>init [MASTER]                  - Initializes the master password</li>
 *     <li>get  [MASTER] [URL]            - Returns the password for a given web address</li>
 *     <li>put  [MASTER] [URL] [PASSWORD] - Sets a password for a given web address</li>
 *     <li>help [COMMAND]                 - Prints the manual for a certain command</li>
 *     <li>exit                           - Terminates the shell</li>
 * </ul>
 */
public class PasswordManager {
    public static void main(String[] args) {
        System.out.println("Welcome to Password Manager v 1.0");

        ShellStatus status = ShellStatus.CONTINUE;
        Environment env = new ShellEnvironment();

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
}
