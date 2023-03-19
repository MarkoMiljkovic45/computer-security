package hr.fer.zemris.java.hw06.shell;

import hr.fer.zemris.java.hw06.shell.commands.ShellCommand;
import hr.fer.zemris.java.hw06.shell.env.Environment;
import hr.fer.zemris.java.hw06.shell.env.ShellEnvironment;
import hr.fer.zemris.java.hw06.shell.env.ShellStatus;
import hr.fer.zemris.java.hw06.shell.util.reader.StringReader;

/**
 * A basic shell program
 *
 * @author Marko Miljković (miljkovicmarko45@gmail.com)
 */
public class MyShell {

    public static void main(String[] args) {
        System.out.println("Welcome to MyShell v 1.0");

        ShellStatus status = ShellStatus.CONTINUE;
        Environment env = new ShellEnvironment();

        while (status != ShellStatus.TERMINATE) {
            env.write(env.getPromptSymbol() + " ");

            String userInput = getUserInputFromEnv(env);

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

    public static String getUserInputFromEnv(Environment env) {
        StringBuilder sb = new StringBuilder();

        String line = env.readLine();

        while (line.endsWith(env.getMorelinesSymbol().toString())) {
            sb.append(line, 0, line.length() - 1);
            env.write(env.getMultilineSymbol() + " ");
            line = env.readLine();
        }

        sb.append(line);

        return sb.toString();
    }
}
