package hr.fer.srs.env;

import hr.fer.srs.commands.*;

import javax.crypto.spec.PBEKeySpec;
import java.util.Collections;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

public class ShellEnvironment implements Environment {

    /**
     * Scanner used to read user input
     */
    private final Scanner envInputScanner;

    /**
     * List of all available commands
     */
    private final SortedMap<String, ShellCommand> commands;

    /**
     * Master password used for encryption
     */
    private PBEKeySpec masterPassword;

    public ShellEnvironment() {
        try {
            envInputScanner = new Scanner(System.in);
        } catch (Exception e) {
            throw new ShellIOException("Error initializing scanner.");
        }

        commands = new TreeMap<>();
        initCommands();

        initPasswords();
    }

    @Override
    public String readLine() throws ShellIOException {
        return envInputScanner.nextLine();
    }

    @Override
    public void write(String text) throws ShellIOException {
        System.out.print(text);
    }

    @Override
    public void writeln(String text) throws ShellIOException {
        System.out.println(text);
    }

    @Override
    public SortedMap<String, ShellCommand> commands() {
        return Collections.unmodifiableSortedMap(commands);
    }

    @Override
    public Character getPromptSymbol() {
        return '>';
    }

    private void initCommands() {
        commands.put("init", new InitShellCommand());
        commands.put("put", new PutShellCommand());
        commands.put("get", new GetShellCommand());
        commands.put("help", new HelpShellCommand());
        commands.put("exit", new ExitShellCommand());
    }

    private void initPasswords() {

    }
}
