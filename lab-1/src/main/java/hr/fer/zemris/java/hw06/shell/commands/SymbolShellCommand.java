package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.env.Environment;
import hr.fer.zemris.java.hw06.shell.env.ShellStatus;
import hr.fer.zemris.java.hw06.shell.util.Util;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Used to change environment symbols
 * <p>
 * It takes two arguments:
 * <ul>
 *     <li>The first is environment symbol name (mandatory)</li>
 *     <li>The second is the new character you want to change the symbol to (optional)</li>
 * </ul>
 * <p>
 * If only one argument is provided the command returns the current character for environment symbol
 * <p>
 * If both arguments are provided, the environment symbol is changed to the new character.
 */
public class SymbolShellCommand implements ShellCommand {
    public SymbolShellCommand() {
    }

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        List<String> args = Util.parse(arguments);

        if (args == null) {
            throw new IllegalArgumentException("Command symbol requires at least one argument.");
        }

        String symbolName = args.get(0);

        if (symbolName.equals("MORELINES")) {
            return executeCommand(env, args, env::getMorelinesSymbol, env::setMorelinesSymbol);
        }

        if (symbolName.equals("PROMPT")) {
            return executeCommand(env, args, env::getPromptSymbol, env::setPromptSymbol);
        }

        if (symbolName.equals("MULTILINE")) {
            return executeCommand(env, args, env::getMultilineSymbol, env::setMultilineSymbol);
        }

        env.writeln("Symbol " + symbolName + " not recognised.");
        return ShellStatus.CONTINUE;
    }

    private ShellStatus executeCommand(Environment env, List<String> args, Supplier<Character> getSymbol, Consumer<Character> setSymbol) {
        String symbolName = args.get(0);
        char oldChar = getSymbol.get();

        if (args.size() == 1) {
            env.writeln("Symbol for " + symbolName + " is '" + oldChar + "'");
        } else {
            char newChar = args.get(1).charAt(0);
            setSymbol.accept(newChar);
            env.writeln("Symbol for " + symbolName + " changed from '" + oldChar + "' to '" + newChar + "'");
        }

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "symbol";
    }

    @Override
    public List<String> getCommandDescription() {
        return List.of(new String[]
                {"symbol - Sets character to represent environment symbol.\n",
                 "symbol [NAME] [NEW]\n",
                 "NAME - Environment symbol name.",
                 "NEW  - New character to represent the environment symbol.",
                 "       If NEW isn't provided, the current character for the",
                 "       environment symbol is printed."}
        );
    }
}
