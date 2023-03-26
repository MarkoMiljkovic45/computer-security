package hr.fer.srs;

import hr.fer.srs.util.Util;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Test {

    public static void main(String[] args) throws NoSuchAlgorithmException {

        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed("master".getBytes(StandardCharsets.UTF_8));
        byte[] arr = new byte[16];
        random.nextBytes(arr);
        System.out.println(Util.byteToHex(arr));
    }
}
