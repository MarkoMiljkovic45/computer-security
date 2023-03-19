package hr.fer.oprpp1.hw05.crypto;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Scanner;

import static hr.fer.oprpp1.hw05.crypto.Util.byteToHex;
import static hr.fer.oprpp1.hw05.crypto.Util.hexToByte;

/**
 * Crypto is a program that will allow the user to encrypt/decrypt a given file using the AES
 * cryptographic algorithm and the 128-bit encryption key or calculate and check the SHA-256 file digest.
 *
 * @author Marko Miljković (miljkovicmarko45@gmail.com)
 */
public class Crypto {

    private static final int BUFFER_SIZE = 4096;

    /**
     * A console program with 3 different modes:
     * <ul>
     *     <li>checksha</li>
     *     <li>encrypt</li>
     *     <li>decrypt</li>
     * </ul>
     * <p>
     * Checksha is used for checking an SHA-256 digest. The user is prompted to provide a 256 bit file digest
     * (64 hex digits) and that digest is then compared do the SHA-256 digest from the provided file.
     * <p>
     * Encrypt is used for encrypting a given file using AES. The user is prompted to provide a 128 bit password
     * (32 hex digits) and a 128 bit initialization vector.
     * <p>
     * Decrypt is used for decrypting a given file using AES. The user is prompted to provide a 128 bit password
     * (32 hex digits) and a 128 bit initialization vector.
     * <p>
     * The mode, source file and an optional target file is provided using program arguments
     *
     * @param args Arguments for program
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Please provide program arguments (mode and source file)!");
            System.exit(0);
        }

        String mode     = args[0];
        Path sourceFile = Path.of(args[1]);
        Path targetFile = null;

        if (args.length == 3) {
            targetFile = Path.of(args[2]);

            if (Files.exists(targetFile) && !Files.isDirectory(targetFile)) {
                System.out.println("WARNING: Target file already exists and will be overwritten!");
            }
        }

        try {

            switch (mode.toLowerCase()) {
                case "checksha" -> checkSHA(sourceFile);
                case "encrypt"  -> encryptAES(sourceFile, targetFile);
                case "decrypt"  -> decryptAES(sourceFile, targetFile);
                default -> System.out.println("Unsupported mode!");
            }

        } catch (Exception e) {
            System.out.println("An error occurred!");
            System.exit(0);
        }
    }

    /**
     * Helper method for checking SHA-256 digest
     *
     * @param sourceFile This files SHA-256 digest will be compared to the user provided digest
     */
    private static void checkSHA(Path sourceFile) {

        try (InputStream sourceFileInputStream = Files.newInputStream(sourceFile);
             Scanner sc = new Scanner(System.in)) {

            System.out.printf("Please provide expected sha-256 digest for %s:\n> ", sourceFile.getFileName());
            String providedHexDigest = sc.nextLine();

            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] buff = new byte[Crypto.BUFFER_SIZE];

            for (int r = sourceFileInputStream.read(buff); r > 0; r = sourceFileInputStream.read(buff)) {
                sha.update(buff, 0, r);
            }
            byte[] actualDigest = sha.digest();
            String actualHexDigest = byteToHex(actualDigest);

            if (actualHexDigest.equals(providedHexDigest.toLowerCase())) {
                System.out.printf("Digesting completed. Digest of %s matches expected digest.", sourceFile.getFileName());
            } else {
                System.out.printf("Digesting completed. Digest of %s does not match the expected digest.\nDigest was: %s\n", sourceFile.getFileName(), actualHexDigest);
            }
        } catch (GeneralSecurityException s) {
            System.out.println("Error initializing SHA-256.");
            System.exit(0);
        } catch (IOException io) {
            System.out.println("Error reading file: " + sourceFile.getFileName() + ".");
            System.exit(0);
        }
    }

    private static void encryptAES(Path sourceFile, Path targetFile) {
        if (targetFile == null) {
            targetFile = Path.of(sourceFile.getFileName() + "_encrypted.bin");
        }
        cipherAES(Cipher.ENCRYPT_MODE, sourceFile, targetFile);
        System.out.printf("Encryption completed. Generated file %s based on file %s.\n", targetFile.getFileName(), sourceFile.getFileName());
    }

    private static void decryptAES(Path sourceFile, Path targetFile) {
        if (targetFile == null) {
            targetFile = Path.of(sourceFile.getFileName() + "_decrypted.bin");
        }
        cipherAES(Cipher.DECRYPT_MODE, sourceFile, targetFile);
        System.out.printf("Decryption completed. Generated file %s based on file %s.\n", targetFile.getFileName(), sourceFile.getFileName());
    }

    private static void cipherAES(int opmode, Path sourceFile, Path targetFile) {
        try (Scanner sc = new Scanner(System.in);
             InputStream sourceFileInputStream = Files.newInputStream(sourceFile);
             OutputStream targetFileOutputStream = Files.newOutputStream(targetFile)) {

            System.out.print("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits):\n> ");
            String keyText = sc.nextLine();

            System.out.print("Please provide initialization vector as hex-encoded text (32 hex-digits):\n> ");
            String ivText = sc.nextLine();

            SecretKeySpec keySpec = new SecretKeySpec(hexToByte(keyText), "AES");
            AlgorithmParameterSpec paramSpec = new IvParameterSpec(hexToByte(ivText));

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(opmode, keySpec, paramSpec);

            byte[] buff = new byte[Crypto.BUFFER_SIZE];

            for(int r = sourceFileInputStream.read(buff); r > 0; r = sourceFileInputStream.read(buff)) {
                if (r < Crypto.BUFFER_SIZE) {
                    targetFileOutputStream.write(cipher.doFinal(buff, 0, r));
                } else {
                    targetFileOutputStream.write(cipher.update(buff));
                }
            }
        } catch (GeneralSecurityException s) {
            System.out.println("Error while initializing AES cipher.");
            System.exit(0);
        } catch (IOException io) {
            System.out.println("Error reading/writing files.");
            System.exit(0);
        }
    }
}
