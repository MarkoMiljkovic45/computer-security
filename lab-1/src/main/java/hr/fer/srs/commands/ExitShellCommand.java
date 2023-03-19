package hr.fer.srs.commands;

import hr.fer.srs.env.Environment;
import hr.fer.srs.env.ShellStatus;

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
