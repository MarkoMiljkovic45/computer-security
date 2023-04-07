package db;

import util.User;
import util.Util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class DatabaseEntry {

    private final String username;
    private final byte[] salt;
    private final byte[] passwordHash;
    private boolean forcepass;

    private static final int SALT_SIZE = 64;
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    public DatabaseEntry(String username, byte[] salt, byte[] passwordHash, boolean forcepass) {
        this.username = username;
        this.salt = salt;
        this.passwordHash = passwordHash;
        this.forcepass = forcepass;
    }

    private DatabaseEntry(String[] entryColumns) {
        this(
                entryColumns[0],
                Util.hexToByte(entryColumns[1]),
                Util.hexToByte(entryColumns[2]),
                entryColumns[3].equals("1")
        );
    }

    public DatabaseEntry(String entry) {
        this(entry.split(" "));
    }

    public String getUsername() {
        return username;
    }

    public byte[] getSalt() {
        return salt;
    }

    public byte[] getPasswordHash() {
        return passwordHash;
    }

    public boolean isForcepass() {
        return forcepass;
    }

    public void setForcepass(boolean forcepass) {
        this.forcepass = forcepass;
    }

    public static DatabaseEntry createFromUser(User user, boolean forcepass) {
        String username = user.username();
        byte[] salt = Util.generateSalt(SALT_SIZE);
        byte[] saltedPassword = Util.toByteArray(user.password().getBytes(CHARSET), salt);
        byte[] passwordHash = Util.hashMessage(saltedPassword);
        return new DatabaseEntry(username, salt, passwordHash, forcepass);
    }

    @Override
    public String toString() {
        return username + " " +
                Util.byteToHex(salt) + " " +
                Util.byteToHex(passwordHash) + " " +
                (forcepass ? "1" : "0");
    }
}
