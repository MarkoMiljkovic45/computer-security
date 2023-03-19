package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.env.Environment;
import hr.fer.zemris.java.hw06.shell.env.ShellStatus;
import hr.fer.zemris.java.hw06.shell.util.Util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

/**
 * The hexdump command expects a single argument: file name, and produces hex-output of the file.
 * <p>
 * On the right side of the output only a standard subset of characters is shown; for all other
 * characters a '.' is printed instead
 * <p>
 * (i.e. replace all bytes whose value is less than 32 or greater than 127 with '.').
 */
public class HexdumpShellCommand implements ShellCommand {
    private static final int BUFFER_SIZE = 16;
    private static final int COUNT = 128;

    public HexdumpShellCommand() {
    }

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        List<String> args = Util.parse(arguments);

        if (args == null) {
            throw new IllegalArgumentException("Command hexdump requires an argument.");
        }

        File source = new File(args.get(0));

        if (source.isDirectory()) {
            throw new IllegalArgumentException("Argument of hexdump must be a file.");
        }

        byte[] buff = new byte[BUFFER_SIZE];

        try (InputStream bis = new BufferedInputStream(Files.newInputStream(source.toPath()), BUFFER_SIZE * COUNT)) {
            int lineNumber = 0;

            for (int r = bis.read(buff); r > 0; r = bis.read(buff)) {
                String line = formatLine(lineNumber, buff, r);

                env.writeln(line);

                lineNumber += r;
            }

        } catch (IOException e) {
            throw new IllegalArgumentException("Error opening file.");
        }

        return ShellStatus.CONTINUE;
    }

    private String formatLine(int lineNumber, byte[] buff, int r) {
        StringBuilder leftSide = new StringBuilder();
        StringBuilder rightSide = new StringBuilder();

        leftSide.append(String.format("%08X:", lineNumber));            //Line number

        for (int i = 0; i < buff.length; i++) {
            byte b = buff[i];

            if (i != buff.length / 2) {
                leftSide.append(" ");
            } else {
                leftSide.append("|");
            }

            if (i < r) {
                leftSide.append(String.format("%02X", b));

                if (b >= 32) {
                    rightSide.append(String.format("%c", b));
                } else {
                    rightSide.append(".");
                }

            } else {
                leftSide.append("  ");
            }
        }

        leftSide.append(" | ").append(rightSide);
        return leftSide.toString();
    }

    @Override
    public String getCommandName() {
        return "hexdump";
    }

    @Override
    public List<String> getCommandDescription() {
        return List.of(new String[]
                {"hexdump - Produces hex-output of a file\n",
                        "hexdump [FILE]\n",
                        "FILE - Path to file."}
        );
    }
}
