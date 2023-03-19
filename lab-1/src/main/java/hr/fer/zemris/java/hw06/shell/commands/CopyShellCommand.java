package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.env.Environment;
import hr.fer.zemris.java.hw06.shell.env.ShellStatus;
import hr.fer.zemris.java.hw06.shell.util.Util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.List;

/**
 * The copy command expects two arguments: source file name and destination file name (i.e. paths and
 * names).
 * <p>
 * If destination file exists, asks user is it allowed to overwrite it.
 * <p>
 * The copy command will work only with files (no directories). If the second argument is directory,
 * the command will assume that user wants to copy the original file into that directory using
 * the original file name.
 *
 * @author Marko Miljković (miljkovicmarko45@gmail.com)
 */
public class CopyShellCommand implements ShellCommand {

    public CopyShellCommand() {
    }

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        List<String> args = Util.parse(arguments);

        if (args.size() != 2) {
            throw new IllegalArgumentException("Command copy requires 2 arguments: copy [FILE] [TARGET]");
        }

        File source = new File(args.get(0));
        File target = new File(args.get(1));

        if (source.isDirectory()) {
            throw new IllegalArgumentException("First argument of copy must be file.");
        }

        if (target.isDirectory()) {
            target = new File(args.get(1) + "/"  + source.getName());
        }

        if (target.exists()) {
            env.write("File " + target + " already exits. Do you want to overwrite it (Y/N)? ");
            String ans = env.readLine().toUpperCase();

            if (ans.equals("N")) {
                return ShellStatus.CONTINUE;
            }
        }

        try (InputStream is = Files.newInputStream(source.toPath());
             OutputStream os = Files.newOutputStream(target.toPath())) {

            is.transferTo(os);

        } catch (IOException e) {
            throw new IllegalArgumentException("Error opening files.");
        }

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "copy";
    }

    @Override
    public List<String> getCommandDescription() {
        return List.of(new String[]
                {"copy - Copy files\n",
                 "copy [FILE] [TARGET]\n",
                 "FILE   - Path to file.",
                 "TARGET - Path to copy of file.",
                 "         If path points to a directory the original file will be copied",
                 "         into that directory with the original file name."}
        );
    }
}
