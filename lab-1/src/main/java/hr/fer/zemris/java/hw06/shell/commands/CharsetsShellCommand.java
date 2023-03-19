package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.env.Environment;
import hr.fer.zemris.java.hw06.shell.env.ShellStatus;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Set;

/**
 * Command charsets takes no arguments and lists names of supported charsets for your Java platform.
 * <p>
 * A single charset name is written per line.
 *
 * @author Marko Miljković (miljkovicmarko45@gmail.com)
 */
public class CharsetsShellCommand implements ShellCommand {

    public CharsetsShellCommand() {
    }

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        Set<String> charsets = Charset.availableCharsets().keySet();

        charsets.forEach(env::writeln);

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "charsets";
    }

    @Override
    public List<String> getCommandDescription() {
        return List.of("charsets - \tLists names of supported charsets for your Java platform.\n");
    }
}
