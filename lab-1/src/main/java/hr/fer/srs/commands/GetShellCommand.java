package hr.fer.srs.commands;

import hr.fer.srs.env.Environment;
import hr.fer.srs.env.ShellIOException;
import hr.fer.srs.env.ShellStatus;
import hr.fer.srs.util.Util;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import java.util.List;

import static hr.fer.srs.PasswordManager.CHARSET;

public class GetShellCommand implements ShellCommand {

    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        List<String> args = Util.parse(arguments);

        if (args.size() != 1) {
            throw new IllegalArgumentException("Command get requires 1 arguments: get [URL]");
        }

        String url = args.get(0);

        try {
            String addressHmac;
            String expectedIntegrityToken;
            String password;

            Mac hmacSha256 = Mac.getInstance("HmacSHA256");
            hmacSha256.init(env.getHmacAddressKey());
            addressHmac = Util.byteToHex(hmacSha256.doFinal(url.getBytes(CHARSET)));

            String databaseEntry          = env.getDatabaseEntry(addressHmac);

            if (databaseEntry == null) {
                throw new ShellIOException("Entry not found or integrity check failed.");
            }

            String[] databaseEntryColumns = databaseEntry.split(" ");

            String encryptedPassword = databaseEntryColumns[1];
            String integrityToken    = databaseEntryColumns[2];

            hmacSha256.init(env.getHmacTokenKey());
            String integrityCheck  = addressHmac + encryptedPassword;
            expectedIntegrityToken = Util.byteToHex(hmacSha256.doFinal(integrityCheck.getBytes(CHARSET)));

            if (!integrityToken.equals(expectedIntegrityToken)) {
                throw new ShellIOException("Integrity check failed!");
            }

            Cipher aes128 = Cipher.getInstance("AES/CBC/PKCS5Padding");
            aes128.init(Cipher.DECRYPT_MODE, env.getAesKey(), new IvParameterSpec(env.getAesIV()));
            String decryptedPassword = new String(aes128.doFinal(Util.hexToByte(encryptedPassword)), CHARSET);

            password = decryptedPassword.split(" ")[0];

            env.writeln("Password for " + url + " is: " + password);
            return ShellStatus.CONTINUE;
        } catch (Exception e) {
            throw new ShellIOException(e.getMessage());
        }
    }

    @Override
    public String getCommandName() {
        return "get";
    }

    @Override
    public List<String> getCommandDescription() {
        return List.of(new String[]
                {"get - Returns the password for a given web address.\n",
                 "get [URL]\n",
                 "URL    - The website whose password you are retrieving"}
        );
    }
}
