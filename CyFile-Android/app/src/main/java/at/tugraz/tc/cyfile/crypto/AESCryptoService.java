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
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import java.util.Base64;

public class AESCryptoService implements CryptoService {
    private Cipher encCipher;
    private Cipher decCipher;

    private static final String ALGO = "AES";
    private static final byte[] keyValue =
            new byte[]{'T', 'h', 'e', 'B', 'e', 's', 't', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y'};


    //is it a problem if we use 0 iv? maybe we should randomize it?
    byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    String password = "hunter2";
    Key key = null;

    public AESCryptoService() {
        try {
            key = generateKey();
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
        return new SecretKeySpec(keyValue, ALGO);
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
            encCipher = Cipher.getInstance(ALGO);
            encCipher.init(Cipher.ENCRYPT_MODE, key);
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

//    @TargetApi(Build.VERSION_CODES.O)
//    @Override
//    public String encrypt(String data) {
//        String encryptedValue = null;
//
//        try {
//            byte[] encValue = encryptCipher.doFinal(data.getBytes());
////            byte[] encryptedByteValue = Base64.encode(encValue, Base64.DEFAULT);
//            encryptedValue = new String(encValue);
//        } catch (IllegalBlockSizeException | BadPaddingException e) {
//            e.printStackTrace();
//        }
//        return encryptedValue;
//    }

    @Override
    public byte[] decrypt(byte[] cipherData) {
        return new byte[0];
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String decrypt(String encryptedData) {
        try {
            Cipher c = Cipher.getInstance(ALGO);
            c.init(Cipher.DECRYPT_MODE, key);
            byte[] decordedValue = Base64.getDecoder().decode(encryptedData);
            byte[] decValue = c.doFinal(decordedValue);
            return new String(decValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    @Override
//    public String decrypt(String cipherData) {
//        String.format("%1$-" + 16 + "s", cipherData);
//        String plainText = null;
//        try {
//            byte[] decrypted = decryptCipher.doFinal(cipherData.getBytes());
//            plainText = new String(decrypted);
//        } catch (IllegalBlockSizeException | BadPaddingException e) {
//            e.printStackTrace();
//        }
//
//        return plainText;
//    }
}
