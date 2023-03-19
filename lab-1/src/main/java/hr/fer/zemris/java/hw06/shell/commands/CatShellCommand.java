package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.env.Environment;
import hr.fer.zemris.java.hw06.shell.env.ShellStatus;
import hr.fer.zemris.java.hw06.shell.util.Util;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Command cat takes one or two arguments. The first argument is path to some file and is mandatory. The
 * second argument is charset name that should be used to interpret the file.
 * <p>
 * If not provided, a default platform charset is used.
 * <p>
 * This command opens given file and writes its content to console.
 *
 * @author Marko Miljković (miljkovicmarko45@gmail.com)
 */
public class CatShellCommand implements ShellCommand {

    private static final int FILE_READER_BUFFER_SIZE = 2048;

    public CatShellCommand() {
    }

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        List<String> args = Util.parse(arguments);

        if (args == null) {
            throw new IllegalArgumentException("Command cat requires at leas one argument.");
        }

        Path file = Path.of(args.get(0));
        Charset cs = Charset.defaultCharset();

        if (args.size() >= 2) {
            cs = Charset.forName(args.get(1));
        }

        char[] buff = new char[FILE_READER_BUFFER_SIZE];

        try (Reader reader = Files.newBufferedReader(file, cs)) {
            for (int r = reader.read(buff); r > 0; r = reader.read(buff)) {
                env.write(new String(buff, 0, r));
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Error reading file: " + file);
        }

        env.write("\n");
        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "cat";
    }

    @Override
    public List<String> getCommandDescription() {
        return List.of(new String[]
                {"cat - This command opens given file and writes its content to console.\n",
                 "cat [FILE] [OPTION]\n",
                 "FILE   - Path to file",
                 "OPTION - Charset that should be used to interpret the file.",
                 "         If not provided, a default platform charset is used."}
        );
    }
}
