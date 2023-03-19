package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.env.Environment;
import hr.fer.zemris.java.hw06.shell.env.ShellStatus;

import java.util.List;

/**
 * Command used to terminate shell
 *
 * @author Marko Miljković (miljkovicmarko45@gmail.com)
 */
public class ExitShellCommand implements ShellCommand {

    public ExitShellCommand() {
    }

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        return ShellStatus.TERMINATE;
    }

    @Override
    public String getCommandName() {
        return "exit";
    }

    @Override
    public List<String> getCommandDescription() {
        return List.of("exit - Terminates the shell\n");
    }
}
