package at.tugraz.tc.cyfile.crypto;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import java.security.SecureRandom;
import java.util.Base64;

public class AESCryptoService implements CryptoService {
    private Cipher encCipher;
    private Cipher decCipher;

    private static final String ALGO = "AES/CBC/PKCS5Padding";
    private static final byte[] keyValue =
            new byte[]{'T', 'h', 'e', 'B', 'e', 's', 't', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y'};


    byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    String password = "hunter2";

    public AESCryptoService() {
        try {
//            MessageDigest digest = MessageDigest.getInstance("SHA-256");
//            digest.update(password.getBytes("UTF-8"));
//            byte[] keyBytes = new byte[16];
//            System.arraycopy(digest.digest(), 0, keyBytes, 0, keyBytes.length);
//            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");

            int ivSize = 16;
            byte[] iv = new byte[ivSize];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            Key key = generateKey();
            encCipher = Cipher.getInstance(ALGO);
            encCipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);

            decCipher = Cipher.getInstance(ALGO);
            decCipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
        } catch (Exception e) {
            //TODO
            e.printStackTrace();
        }

//        try {
//            //don't actually use MD5 :P
//            MessageDigest digester = MessageDigest.getInstance("MD5");
//            digester.update(password.getBytes("UTF-8"));
//            byte[] keyData = digester.digest();
//            SecretKeySpec key = new SecretKeySpec(keyData, "AES");
//
//            IvParameterSpec ivSpec = new IvParameterSpec(iv);
//
//            encryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//            encryptCipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
//
//            decryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//            decryptCipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
//        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
    }

    private static Key generateKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        SecretKey key = keyGenerator.generateKey();
        return key;
    }

    /**
     * Encrypt a string with AES algorithm.
     *
     * @param data is a string
     * @return the encrypted string
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public String encrypt(String data) {
        try {
            byte[] encVal = encCipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encVal);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public byte[] encrypt(byte[] data) {
        return new byte[0];
    }

    @Override
    public byte[] decrypt(byte[] cipherData) {
        return new byte[0];
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String decrypt(String encryptedData) {
        try {
            byte[] decodedValue = Base64.getDecoder().decode(encryptedData);
            byte[] decValue = decCipher.doFinal(decodedValue);
            return new String(decValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
