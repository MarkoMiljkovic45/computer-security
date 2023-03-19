package hr.fer.zemris.java.hw06.shell.env;

import hr.fer.zemris.java.hw06.shell.commands.*;

import java.util.Collections;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

public class ShellEnvironment implements Environment {

    /**
     * Character denoting that a command will span across multiple lines
     */
    private Character MORELINESSYMBOL;

    /**
     * Character denoting the beginning of the shells first user input line
     */
    private Character PROMPTSYMBOL;

    /**
     * If a command spans across multiple lines this character denotes every line after the first
     */
    private Character MULTILINESYMBOL;

    /**
     * Scanner used to read user input
     */
    private final Scanner envInputScanner;

    /**
     * List of all available commands
     */
    private final SortedMap<String, ShellCommand> commands;

    public ShellEnvironment() {
        setMorelinesSymbol('/');
        setPromptSymbol('>');
        setMultilineSymbol('|');

        try {
            envInputScanner = new Scanner(System.in);
        } catch (Exception e) {
            throw new ShellIOException("Error initializing scanner.");
        }

        commands = new TreeMap<>();
        commands.put("cat", new CatShellCommand());
        commands.put("charsets", new CharsetsShellCommand());
        commands.put("copy", new CopyShellCommand());
        commands.put("exit", new ExitShellCommand());
        commands.put("help", new HelpShellCommand());
        commands.put("hexdump", new HexdumpShellCommand());
        commands.put("ls", new LsShellCommand());
        commands.put("mkdir", new MkdirShellCommand());
        commands.put("symbol", new SymbolShellCommand());
        commands.put("tree", new TreeShellCommand());
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
    public Character getMultilineSymbol() {
        return MULTILINESYMBOL;
    }

    @Override
    public void setMultilineSymbol(Character symbol) {
        this.MULTILINESYMBOL = symbol;
    }

    @Override
    public Character getPromptSymbol() {
        return PROMPTSYMBOL;
    }

    @Override
    public void setPromptSymbol(Character symbol) {
        this.PROMPTSYMBOL = symbol;
    }

    @Override
    public Character getMorelinesSymbol() {
        return MORELINESSYMBOL;
    }

    @Override
    public void setMorelinesSymbol(Character symbol) {
        this.MORELINESSYMBOL = symbol;
    }
}
