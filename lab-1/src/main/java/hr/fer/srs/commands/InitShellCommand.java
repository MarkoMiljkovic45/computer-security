package hr.fer.srs.commands;

import hr.fer.srs.env.Environment;
import hr.fer.srs.env.ShellStatus;

import java.util.List;

public class InitShellCommand implements ShellCommand {

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        return null;
    }

    @Override
    public String getCommandName() {
        return null;
    }

    @Override
    public List<String> getCommandDescription() {
        return List.of(new String[]
                {"init - Initializes the master password.\n",
                 "init [MASTER]\n",
                 "MASTER - Master password"}
        );
    }
}
