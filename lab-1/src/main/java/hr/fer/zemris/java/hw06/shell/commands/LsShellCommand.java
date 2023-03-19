package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.env.Environment;
import hr.fer.zemris.java.hw06.shell.env.ShellStatus;
import hr.fer.zemris.java.hw06.shell.util.Util;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Command ls takes a single argument: directory, and writes a directory listing (not recursive).
 *<p>
 * The output consists of 4 columns:
 * <ul>
 *     <li>
 *         First column indicates if current object is directory (d), readable (r), writable (w)
 *         and executable (x).
 *     </li>
 *     <li>
 *         Second column contains object size in bytes that is right aligned and occupies 10 characters.
 *     </li>
 *     <li>
 *         Third column contains file creation date/time
 *     </li>
 *     <li>
 *         Fourth column contains file name
 *     </li>
 * </ul>
 */
public class LsShellCommand implements ShellCommand {

    public LsShellCommand() {
    }

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        List<String> args = Util.parse(arguments);

        if (args == null) {
            throw new IllegalArgumentException("Command ls requires an argument.");
        }

        Path dir = Path.of(args.get(0));

        if (!Files.isDirectory(dir)) {
            throw new IllegalArgumentException("Argument of ls must be a directory");
        }

        try (DirectoryStream<Path> ds = Files.newDirectoryStream(dir)) {
            for (Path p: ds) {
                String fileData = getFileDescription(p);
                env.writeln(fileData);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while reading directory.");
        }

        return ShellStatus.CONTINUE;
    }

    private String getFileDescription(Path p) throws IOException {
        StringBuilder sb = new StringBuilder();

        if (Files.isDirectory(p)) {
            sb.append("d");
        } else {
            sb.append("-");
        }

        if (Files.isReadable(p)) {
            sb.append("r");
        } else {
            sb.append("-");
        }

        if (Files.isWritable(p)) {
            sb.append("w");
        } else {
            sb.append("-");
        }

        if (Files.isExecutable(p)) {
            sb.append("x");
        } else {
            sb.append("-");
        }

        sb.append(String.format(" %10d ", Files.size(p)));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        BasicFileAttributeView faView = Files.getFileAttributeView(
                p, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS
        );
        BasicFileAttributes attributes = faView.readAttributes();
        FileTime fileTime = attributes.creationTime();
        String formattedDateTime = sdf.format(new Date(fileTime.toMillis()));

        sb.append(formattedDateTime).append(" ");

        sb.append(p.getFileName());

        return sb.toString();
    }

    @Override
    public String getCommandName() {
        return "ls";
    }

    @Override
    public List<String> getCommandDescription() {
        return List.of(new String[]
                {"ls - Writes a directory listing (not recursive).\n",
                 "ls [DIR]\n",
                 "DIR - Path to directory."}
        );
    }
}
