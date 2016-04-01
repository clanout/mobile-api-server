package com.clanout.apiserver;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.SecureRandom;

public class Test
{
    private static final String ALGORITHM = "AES";

    public static void main(String[] args) throws Exception
    {
        String keyStr = "Tomriddlediary";
        Key key = generateKeyFromString(keyStr);
        System.out.println("Key = " + new String(key.getEncoded()));
        String value = "Hello, World!";

        long startTime;
        long endTime;

        // Encryption
        startTime = System.currentTimeMillis();
        String encrypted = encrypt(value, key);
        endTime = System.currentTimeMillis();

        System.out.println("Encryption Time = " + (endTime - startTime) + "ms");
        System.out.println("Encrypted Value = " + encrypted);

        // Decryption
        startTime = System.currentTimeMillis();
        String decrypted = decrypt(encrypted, key);
        endTime = System.currentTimeMillis();

        System.out.println("Decryption Time = " + (endTime - startTime) + "ms");
        System.out.println("Decrypted Value = " + decrypted);

    }

    public static String encrypt(final String valueEnc, Key key)
    {
        String encryptedValue = null;

        try
        {
            final Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.ENCRYPT_MODE, key);
            final byte[] encValue = c.doFinal(valueEnc.getBytes());
            encryptedValue = new BASE64Encoder().encode(encValue);
        }
        catch (Exception ex)
        {
            System.out.println("The Exception is=" + ex);
        }

        return encryptedValue;
    }

    public static String decrypt(final String encryptedValue, Key key)
    {

        String decryptedValue = null;

        try
        {
            final Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.DECRYPT_MODE, key);
            final byte[] decorVal = new BASE64Decoder().decodeBuffer(encryptedValue);
            final byte[] decValue = c.doFinal(decorVal);
            decryptedValue = new String(decValue);
        }
        catch (Exception ex)
        {
            System.out.println("The Exception is=" + ex);
        }

        return decryptedValue;
    }

    private static Key generateKeyFromString(String seed) throws Exception
    {
        KeyGenerator generator = KeyGenerator.getInstance(ALGORITHM);
        generator.init(128, new SecureRandom(seed.getBytes()));
        return generator.generateKey();
    }
}
