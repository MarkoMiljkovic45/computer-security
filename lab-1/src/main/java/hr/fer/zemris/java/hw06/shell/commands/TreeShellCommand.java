package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.env.Environment;
import hr.fer.zemris.java.hw06.shell.env.ShellStatus;
import hr.fer.zemris.java.hw06.shell.util.Util;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

/**
 * The tree command expects a single argument: directory name and prints a tree (each directory
 * level shifts output two characters to the right).
 */
public class TreeShellCommand implements ShellCommand {

    public TreeShellCommand() {
    }

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        List<String> args = Util.parse(arguments);

        if (args == null) {
            throw new IllegalArgumentException("Command tree requires an argument.");
        }

        Path dir = Path.of(args.get(0));

        if (!Files.isDirectory(dir)) {
            throw new IllegalArgumentException("Argument of tree must be a directory.");
        }

        try {
            Files.walkFileTree(dir, new TreeFileVisitor(env));
        } catch (IOException e) {
            throw new IllegalArgumentException("Error opening file.");
        }

        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return "tree";
    }

    @Override
    public List<String> getCommandDescription() {
        return List.of(new String[]
                {"tree - Prints directory tree\n",
                        "tree [DIR]\n",
                        "DIR - Path to directory."}
        );
    }

    /**
     * An implementation of FileVisitor used by the TreeShellCommand
     */
    private static class TreeFileVisitor extends SimpleFileVisitor<Path> {
        /**
         * Current directory depth
         */
        private int depth;
        /**
         * Environment to which output will be sent
         */
        private final Environment env;

        private final static int INDENT = 2;
        protected TreeFileVisitor(Environment env, int startDepth) {
            this.depth = startDepth;
            this.env = env;
        }

        protected TreeFileVisitor(Environment env) {
            this(env, 0);
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
            env.writeln(" ".repeat(depth * INDENT) + dir.getFileName());
            depth++;
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
            env.writeln(" ".repeat(depth * INDENT) + file.getFileName());
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
            depth--;
            return FileVisitResult.CONTINUE;
        }
    }
}

