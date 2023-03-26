package hr.fer.srs.commands;

import hr.fer.srs.env.Environment;
import hr.fer.srs.env.ShellStatus;
import hr.fer.srs.util.Util;

import java.util.List;

public class InitShellCommand implements ShellCommand {

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        List<String> args = Util.parse(arguments);

        if (args.size() != 1) {
            throw new IllegalArgumentException("Command init requires 1 arguments: init [MASTER]");
        }

        env.setMasterPassword(args.get(0));

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "init";
    }

    @Override
    public List<String> getCommandDescription() {
        return List.of(new String[]
                {"init - Initializes the master password.\n",
                 "       WARNING: This will delete the previous database",
                 "init [MASTER]\n",
                 "MASTER - Master password"}
        );
    }
}
