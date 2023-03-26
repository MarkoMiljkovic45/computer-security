package hr.fer.srs.env;

import hr.fer.srs.commands.ShellCommand;

import javax.crypto.spec.SecretKeySpec;
import java.util.List;
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
     * Reads the environment database
     * @return List of lines from the database
     * @throws ShellIOException If an IOException occurs
     */
    List<String> readDatabase() throws ShellIOException;

    /**
     * Writes to the environment database
     * @param text to be written to the database
     * @throws ShellIOException If an IOException occurs
     */
    void writeToDatabase(String text) throws ShellIOException;

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
     * @return Environment hmac key for password address encryption
     */
    SecretKeySpec getHmacAddressKey();

    /**
     * @return Environment hmac key for integrity token generation
     */
    SecretKeySpec getHmacTokenKey();

    /**
     * @return Environment AES128 key
     */
    SecretKeySpec getAesKey();

    /**
     * @return Environment AES128 initialization vector
     */
    byte[] getAesIV();

    /**
     *
     */
    void setMasterPassword(String masterPassword);
}
