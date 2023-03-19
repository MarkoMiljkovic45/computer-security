package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.env.Environment;
import hr.fer.zemris.java.hw06.shell.env.ShellStatus;
import hr.fer.zemris.java.hw06.shell.util.Util;

import java.io.File;
import java.util.List;

/**
 * The mkdir command takes a single argument: directory name, and creates the appropriate directory
 * structure.
 */
public class MkdirShellCommand implements ShellCommand {

    public MkdirShellCommand() {
    }

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        List<String> args = Util.parse(arguments);

        if (args == null) {
            throw new IllegalArgumentException("Command mkdir requires an argument.");
        }

        File dir = new File(args.get(0));

        if (!dir.mkdirs()) {
            throw new RuntimeException("Could not create directory structure.");
        }

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "mkdir";
    }

    @Override
    public List<String> getCommandDescription() {
        return List.of(new String[]
                {"mkdir - Creates appropriate directory structure.\n",
                        "mkdir [DIR]\n",
                        "DIR - Directory structure"}
        );
    }
}
