package hr.fer.srs.commands;

import hr.fer.srs.env.Environment;
import hr.fer.srs.env.ShellStatus;

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
