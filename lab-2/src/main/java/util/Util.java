package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Util {

    public static byte[] hexToByte(String keyText) {
        int keyTextLen = keyText.length();

        if (keyTextLen % 2 != 0)
            throw new IllegalArgumentException("Invalid argument for hexToByte: " + keyText);

        byte[] arr = new byte[keyTextLen / 2];

        try {
            int j = 0;
            for (int i = 0; i <= keyTextLen - 2; i += 2) {
                arr[j++] = (byte) Integer.parseInt(keyText.substring(i, i+2), 16);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(e);
        }

        return arr;
    }

    public static String byteToHex(byte[] byteArray) {
        StringBuilder sb = new StringBuilder();

        for (byte b: byteArray) {
            if (b <= 15 && b >= 0)
                sb.append("0");

            sb.append(Integer.toHexString(Byte.toUnsignedInt(b)));
        }

        return sb.toString();
    }

    public static byte[] generateSalt(int size) {
        byte[] salt = new byte[size];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        return salt;
    }

    public static byte[] hashMessage(byte[] message) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(message);
        }
        catch (NoSuchAlgorithmException ignore) {
            return null;
        }
    }
}
