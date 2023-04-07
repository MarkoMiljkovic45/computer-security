package db;

import util.User;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Database {

    private static final Path DATABASE_PATH = Path.of("database.txt");
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    public static boolean containsEntry(String username) throws IOException {
        if (!Files.exists(DATABASE_PATH)) {
            return false;
        }

        Scanner scanner = new Scanner(DATABASE_PATH);

        while(scanner.hasNext()) {
            String line = scanner.nextLine();

            if (line.startsWith(username)) {
                return true;
            }
        }

        return false;
    }

    public static void deleteEntry(String username) throws IOException {
        if (!Files.exists(DATABASE_PATH)) {
            return;
        }

        List<String> oldLines = Files.readAllLines(DATABASE_PATH);
        List<String> newLines = oldLines.stream().filter(line -> !line.startsWith(username)).toList();
        Files.write(DATABASE_PATH, newLines, CHARSET, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public static void addEntry(User user, boolean forcepass) throws IOException {
        DatabaseEntry entry = DatabaseEntry.createFromUser(user, forcepass);

        Files.write(DATABASE_PATH,
                List.of(entry.toString()),
                CHARSET,
                StandardOpenOption.APPEND,
                StandardOpenOption.CREATE
        );
    }

    public static void addEntry(User user) throws IOException {
        addEntry(user, false);
    }

    public static void setForcepass(String username, boolean forcepass) throws IOException {
        if (!Files.exists(DATABASE_PATH)) {
            return;
        }

        List<String> lines = Files.readAllLines(DATABASE_PATH);
        Optional<String> user = lines.stream().filter(line -> line.startsWith(username)).findAny();

        if (user.isEmpty()) {
            return;
        }

        String userEntry = user.get();
        lines.remove(userEntry);

        DatabaseEntry entry = new DatabaseEntry(userEntry);
        entry.setForcepass(forcepass);

        lines.add(entry.toString());

        Files.write(DATABASE_PATH, lines, CHARSET, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public static DatabaseEntry getEntry(String username) throws IOException {
        if (!Files.exists(DATABASE_PATH)) {
            return null;
        }

        Scanner scanner = new Scanner(DATABASE_PATH);

        while(scanner.hasNext()) {
            String line = scanner.nextLine();

            if (line.startsWith(username)) {
                return new DatabaseEntry(line);
            }
        }

        return null;
    }
}
