package hr.fer.srs.env;

import hr.fer.srs.commands.ShellCommand;

import java.util.SortedMap;

/**
 * Describes an environment that can interact with the user through a console
 *
 * @author Marko Miljković (miljkovicmarko45@gmail.com)
 */
public interface Environment {
    /**
     * Reads a line from the console input
     *
     * @return A line from the console input
     * @throws ShellIOException If there are exceptions while reading the line
     */
    String readLine() throws ShellIOException;

    /**
     * Writes formatted text to the console
     *
     * @param text That will be written to the console
     * @throws ShellIOException If there are exceptions while writing to the console
     */
    void write(String text) throws ShellIOException;

    /**
     * Writes a line to the console
     *
     * @param text Line of text that will be written to the console
     * @throws ShellIOException If there are exceptions while writing to the console
     */
    void writeln(String text) throws ShellIOException;

    /**
     * Lists all available commands
     *
     * @return An unmodifiable map of command name and ShellCommand object pairs
     */
    SortedMap<String, ShellCommand> commands();

    /**
     * This character is used to denote the beginning of the user input area of the shell
     *
     * @return The PROMPTSYMBOL character
     */
    Character getPromptSymbol();

    /**
     * Used to set the PROMPTSYMBOL character
     *
     * @param symbol The new PROMPTSYMBOL character
     */
    void setPromptSymbol(Character symbol);
}
