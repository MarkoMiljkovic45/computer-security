package hr.fer.srs.commands;

import hr.fer.srs.env.Environment;
import hr.fer.srs.env.ShellIOException;
import hr.fer.srs.env.ShellStatus;
import hr.fer.srs.util.Util;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import java.security.SecureRandom;
import java.util.List;

import static hr.fer.srs.PasswordManager.*;


public class PutShellCommand implements ShellCommand {

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        List<String> args = Util.parse(arguments);

        if (args.size() != 2) {
            throw new IllegalArgumentException("Command put requires 2 arguments: put [URL] [PASSWORD]");
        }

        String url      = args.get(0);
        String password = args.get(1);

        if (password.length() > PASSWORD_LENGTH) {
            throw new IllegalArgumentException("Password must be shorter than 256 characters");
        }

        try {
            String addressHmac;
            String encryptedPassword;
            String integrityToken;

            Mac hmacSha256 = Mac.getInstance("HmacSHA256");
            hmacSha256.init(env.getHmacAddressKey());
            addressHmac = Util.byteToHex(hmacSha256.doFinal(url.getBytes(CHARSET)));

            SecureRandom random = new SecureRandom();
            int saltLength      = PASSWORD_LENGTH - password.length() + PASSWORD_PADDING - 1;
            byte[] passwordSalt = new byte[saltLength];

            random.nextBytes(passwordSalt);
            String passwordSaltHex = Util.byteToHex(passwordSalt);
            String saltedPassword  = password + " " + passwordSaltHex;

            Cipher aes128 = Cipher.getInstance("AES/CBC/PKCS5Padding");
            aes128.init(Cipher.ENCRYPT_MODE, env.getAesKey(), new IvParameterSpec(env.getAesIV()));
            encryptedPassword = Util.byteToHex(aes128.doFinal(saltedPassword.getBytes(CHARSET)));

            hmacSha256.init(env.getHmacTokenKey());
            String integrityCheck = addressHmac + encryptedPassword;
            integrityToken        = Util.byteToHex(hmacSha256.doFinal(integrityCheck.getBytes(CHARSET)));

            if (env.containsDatabaseEntry(addressHmac)) {
                env.removeDatabaseEntry(addressHmac);
            }

            env.writeToDatabase(addressHmac + " " + encryptedPassword + " " + integrityToken + "\n");
            return ShellStatus.CONTINUE;
        } catch (Exception e) {
            throw new ShellIOException(e.getMessage());
        }
    }

    @Override
    public String getCommandName() {
        return "put";
    }

    @Override
    public List<String> getCommandDescription() {
        return List.of(new String[]
                {"put - Sets a password for a given web address.\n",
                 "put [URL] [PASSWORD]\n",
                 "URL      - The website whose password you are setting",
                 "PASSWORD - The password for the website"}
        );
    }
}
