package at.tugraz.tc.cyfile.crypto;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

public class AESCryptoService implements CryptoService {
    private Cipher encCipher;
    private Cipher decCipher;

    private static final String ALGO = "AES/CBC/PKCS5Padding";

    public AESCryptoService() {
        int ivSize = 16;
        byte[] iv = new byte[ivSize];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        try {
            Key key = generateKey();
            encCipher = Cipher.getInstance(ALGO);
            encCipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);

            decCipher = Cipher.getInstance(ALGO);
            decCipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException | InvalidKeyException e) {
            //this should never happen...
            //TODO think about error handling, maybe add a init function with a return value or something like that
            e.printStackTrace();
        }
    }

    private static Key generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        return keyGenerator.generateKey();
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
        byte[] encryptedBytes = encrypt(data.getBytes());
        return encryptedBytes.length > 0 ?
                Base64.getEncoder().encodeToString(encryptedBytes)
                : null;
    }

    @Override
    public byte[] encrypt(byte[] data) {
        try {
            return encCipher.doFinal(data);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public byte[] decrypt(byte[] cipherData) {
        try {
            return decCipher.doFinal(cipherData);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String decrypt(String encryptedData) {
        byte[] decodedValue = Base64.getDecoder().decode(encryptedData);
        byte[] decValue = decrypt(decodedValue);
        return new String(decValue);
    }
}
