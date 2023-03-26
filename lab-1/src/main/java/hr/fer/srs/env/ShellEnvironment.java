package hr.fer.srs.env;

import hr.fer.srs.commands.*;
import hr.fer.srs.util.Util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.*;

import static hr.fer.srs.PasswordManager.CHARSET;

public class ShellEnvironment implements Environment {

    /**
     * Scanner used to read user input
     */
    private final Scanner envInputScanner;

    /**
     * List of all available commands
     */
    private final SortedMap<String, ShellCommand> commands;

    /**
     * Environment hmac key for password address encryption
     */
    private SecretKeySpec hmacAddressKey;

    /**
     * Environment hmac key for integrity token generation
     */
    private SecretKeySpec hmacTokenKey;

    /**
     * Environment AES128 key
     */
    private SecretKeySpec aesKey;

    /**
     * Environment AES128 initialization vector
     */
    private byte[] aesIV;


    private static final Path DATABASE_PATH  = Path.of("database.txt");
    private static final int ITERATION_COUNT = 65536;
    private static final int AES_KEY_LENGTH  = 128;
    private static final int HMAC_KEY_LENGTH = 512;

    public ShellEnvironment() {
        try {
            envInputScanner = new Scanner(System.in);
            commands = new TreeMap<>();
            initCommands();
        } catch (Exception e) {
            throw new ShellIOException(e.getMessage());
        }
    }

    @Override
    public String readLine() throws ShellIOException {
        return envInputScanner.nextLine();
    }

    @Override
    public void write(String text) throws ShellIOException {
        System.out.print(text);
    }

    @Override
    public void writeln(String text) throws ShellIOException {
        System.out.println(text);
    }

    @Override
    public List<String> readDatabase() throws ShellIOException {
        try {
            return Files.readAllLines(DATABASE_PATH);
        } catch (Exception e) {
            throw new ShellIOException(e.getMessage());
        }
    }

    @Override
    public void writeToDatabase(String text) throws ShellIOException {
        try {
            Files.writeString(DATABASE_PATH, text, CHARSET, StandardOpenOption.WRITE, StandardOpenOption.APPEND);
        } catch (Exception e) {
            throw new ShellIOException(e.getMessage());
        }
    }

    @Override
    public boolean containsDatabaseEntry(String key) throws ShellIOException {
        try {
            List<String> entries = Files.readAllLines(DATABASE_PATH);
            return entries.stream().anyMatch(line -> line.startsWith(key));
        } catch (Exception e) {
            throw new ShellIOException(e.getMessage());
        }
    }

    @Override
    public void removeDatabaseEntry(String key) throws ShellIOException {
        try {
            List<String> entries = Files.readAllLines(DATABASE_PATH);
            BufferedWriter writer = Files.newBufferedWriter(DATABASE_PATH, StandardOpenOption.TRUNCATE_EXISTING);

            entries.stream()
                    .filter(line -> !line.startsWith(key))
                    .forEach(line -> {
                        try {
                            writer.write(line + "\n");
                        } catch (IOException e) {
                            throw new ShellIOException(e.getMessage());
                        }
                    });

            writer.flush();
            writer.close();
        } catch (Exception e) {
            throw new ShellIOException(e.getMessage());
        }
    }

    @Override
    public String getDatabaseEntry(String key) throws ShellIOException {
        try {
            List<String> entries = Files.readAllLines(DATABASE_PATH);
            Optional<String> databaseEntry = entries.stream().filter(line -> line.startsWith(key)).findAny();
            return databaseEntry.orElse(null);
        } catch (Exception e) {
            throw new ShellIOException(e.getMessage());
        }
    }

    @Override
    public SortedMap<String, ShellCommand> commands() {
        return Collections.unmodifiableSortedMap(commands);
    }

    @Override
    public SecretKeySpec getHmacAddressKey() {
        return hmacAddressKey;
    }

    @Override
    public SecretKeySpec getHmacTokenKey() {
        return hmacTokenKey;
    }

    @Override
    public SecretKeySpec getAesKey() {
        return aesKey;
    }

    @Override
    public byte[] getAesIV() {
        return aesIV;
    }

    @Override
    public Character getPromptSymbol() {
        return '>';
    }

    @Override
    public void setMasterPassword(String masterPassword) {
        try {
            generateSecretKeys(masterPassword);

            MessageDigest sha256      = MessageDigest.getInstance("SHA-256");
            byte[] masterPasswordHash = sha256.digest(masterPassword.getBytes(CHARSET));

            Files.writeString(DATABASE_PATH, Util.byteToHex(masterPasswordHash) + "\n", CHARSET);
        } catch (Exception e) {
            throw new ShellIOException(e.getMessage());
        }
    }

    private void initCommands() {
        commands.put("init", new InitShellCommand());
        commands.put("put", new PutShellCommand());
        commands.put("get", new GetShellCommand());
        commands.put("help", new HelpShellCommand());
        commands.put("exit", new ExitShellCommand());
    }

    public void initSecretKeys() throws ShellIOException {
        try {
            if (Files.exists(DATABASE_PATH)) {
                loadMasterPassword();
            } else {
                initMasterPassword();
            }
        } catch (Exception e) {
            throw new ShellIOException(e.getMessage());
        }
    }

    private void loadMasterPassword() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        System.out.print("Enter master password: ");

        String masterPassword     = envInputScanner.nextLine();
        MessageDigest sha256      = MessageDigest.getInstance("SHA-256");
        byte[] masterPasswordHash = sha256.digest(masterPassword.getBytes(CHARSET));

        Scanner sc              = new Scanner(DATABASE_PATH);
        String storedMasterHash = sc.nextLine();

        if (!Arrays.equals(masterPasswordHash, Util.hexToByte(storedMasterHash))) {
            throw new ShellIOException("Master password incorrect or integrity check failed.");
        }

        generateSecretKeys(masterPassword);
    }

    private void initMasterPassword() {
        System.out.print("Initialize the master password: ");
        String masterPassword = envInputScanner.nextLine();
        setMasterPassword(masterPassword);
    }

    private void generateSecretKeys(String masterPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
        char[] masterPasswordCharArray = masterPassword.toCharArray();

        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(masterPassword.getBytes(CHARSET));

        byte[] hmacAddressSalt = new byte[16];
        byte[] hmacTokenSalt   = new byte[16];
        byte[] aesSalt         = new byte[16];
        byte[] aesIv           = new byte[16];

        random.nextBytes(hmacAddressSalt);
        random.nextBytes(hmacTokenSalt);
        random.nextBytes(aesSalt);
        random.nextBytes(aesIv);

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

        KeySpec hmacAddressKeySpec = new PBEKeySpec(masterPasswordCharArray, hmacAddressSalt, ITERATION_COUNT, HMAC_KEY_LENGTH);
        KeySpec hmacTokenKeySpec   = new PBEKeySpec(masterPasswordCharArray, hmacTokenSalt  , ITERATION_COUNT, HMAC_KEY_LENGTH);
        KeySpec aesKeySpec         = new PBEKeySpec(masterPasswordCharArray, aesSalt        , ITERATION_COUNT, AES_KEY_LENGTH);

        hmacAddressKey = new SecretKeySpec(factory.generateSecret(hmacAddressKeySpec).getEncoded(), "HmacSHA256");
        hmacTokenKey   = new SecretKeySpec(factory.generateSecret(hmacTokenKeySpec).getEncoded()  , "HmacSHA256");
        aesKey         = new SecretKeySpec(factory.generateSecret(aesKeySpec).getEncoded()        , "AES");
        this.aesIV     = aesIv;
    }
}
