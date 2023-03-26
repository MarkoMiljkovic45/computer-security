package hr.fer.srs.commands;

import hr.fer.srs.env.Environment;
import hr.fer.srs.env.ShellStatus;

import java.util.List;

public class PutShellCommand implements ShellCommand {

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
                {"put - Sets a password for a given web address.\n",
                 "put [URL] [PASSWORD]\n",
                 "URL      - The website whose password you are setting",
                 "PASSWORD - The password for the website"}
        );
    }
}
