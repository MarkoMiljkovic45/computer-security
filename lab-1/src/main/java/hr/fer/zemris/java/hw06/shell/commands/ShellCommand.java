package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.env.Environment;
import hr.fer.zemris.java.hw06.shell.env.ShellStatus;

import java.util.List;

/**
 * Represents a Shell command
 *
 * @author Marko Miljković (miljkovicmarko45@gmail.com)
 */
public interface ShellCommand {
    /**
     * Used to execute a command
     *
     * @param env Environment in which the command will be executed
     * @param arguments Arguments for the command
     * @return ShellStatus after command execution
     */
    ShellStatus executeCommand(Environment env, String arguments);

    /**
     * @return command name
     */
    String getCommandName();

    /**
     * Describes the functionality of the command and which arguments can be used
     *
     * @return List of lines containing command documentation
     */
    List<String> getCommandDescription();
}
