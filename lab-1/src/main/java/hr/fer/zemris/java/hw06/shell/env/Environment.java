package hr.fer.zemris.java.hw06.shell.env;

import hr.fer.zemris.java.hw06.shell.commands.ShellCommand;

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
     * This character is used to denote that a command will span across multiple lines
     *
     * @return The MULTILINE character
     */
    Character getMultilineSymbol();

    /**
     * Used to set the MULTILINESYMBOL character
     *
     * @param symbol The new MULTILINESYMBOL character
     */
    void setMultilineSymbol(Character symbol);

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

    /**
     * If a command spans across multiple lines this character is used to denote each row after
     * the first line.
     * <p>
     * The first line is denoted by the PROMPTSYMBOL character
     *
     * @return The MORELINESSYMBOL character
     */
    Character getMorelinesSymbol();

    /**
     * Used to set the MORELINESSYMBOL character
     *
     * @param symbol The new MORELINESSYMBOL character
     */
    void setMorelinesSymbol(Character symbol);

}
