package db;

import util.User;
import util.Util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class DatabaseEntry {

    private String username;
    private byte[] salt;
    private byte[] passwordHash;
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

    public DatabaseEntry(User user, boolean forcepass) {
        try {
            this.username = user.username();
            this.salt = Util.generateSalt(SALT_SIZE);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bos.write(user.password().getBytes(CHARSET));
            bos.write(salt);

            this.passwordHash = Util.hashMessage(bos.toByteArray());
            this.forcepass = forcepass;
        }
        catch (IOException ignore) {}
    }

    public void setForcepass(boolean forcepass) {
        this.forcepass = forcepass;
    }

    @Override
    public String toString() {
        return username + " " +
                Util.byteToHex(salt) + " " +
                Util.byteToHex(passwordHash) + " " +
                (forcepass ? "1" : "0");
    }
}
