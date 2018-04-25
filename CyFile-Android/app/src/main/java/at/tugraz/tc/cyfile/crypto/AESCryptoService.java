package at.tugraz.tc.cyfile.crypto;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.inject.Inject;

public class AESCryptoService implements CryptoService {
    private Cipher encCipher;
    private Cipher decCipher;
    private KeyVaultService keyVaultService;

    private static final String ALGO = "AES/CBC/PKCS5Padding";

    //TODO move to enc-dec
    byte[] iv;

    public AESCryptoService(KeyVaultService keyVaultService) {
        this.keyVaultService = keyVaultService;
    }

    public void init(String passphrase) throws InvalidKeyException {
        keyVaultService.unlockVault(passphrase, ALGO);
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

    private void initEncryptionCipher() {
        Key key = keyVaultService.getEncryptionKey();
        int ivSize = 16;
        iv = new byte[ivSize];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        try {
            encCipher = Cipher.getInstance(ALGO);
            encCipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException | InvalidKeyException e) {
            //this should never happen...
            //TODO think about error handling, maybe add a init function with a return value or something like that
            e.printStackTrace();
        }
    }

    private void initDecryptionCipher(byte[] iv) {
        Key key = keyVaultService.getEncryptionKey();
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        try {
            decCipher = Cipher.getInstance(ALGO);
            decCipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException | InvalidKeyException e) {
            //this should never happen...
            //TODO think about error handling, maybe add a init function with a return value or something like that
            e.printStackTrace();
        }
    }

    @Override
    public byte[] encrypt(byte[] data) {
        try {
            initEncryptionCipher();
            byte[] encBytes = encCipher.doFinal(data);
            byte[] ret = new byte[encBytes.length + iv.length];
            System.arraycopy(iv, 0, ret, 0, iv.length);
            System.arraycopy(encBytes, 0, ret, iv.length, encBytes.length);
            return ret;
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public byte[] decrypt(byte[] cipherData) {
        try {
            byte[] iv = new byte[16];
            byte[] encryptedData = new byte[cipherData.length - 16];
            System.arraycopy(cipherData, 0, iv, 0, 16);
            System.arraycopy(cipherData, 16, encryptedData, 0,
                    cipherData.length - 16);
            initDecryptionCipher(iv);
            return decCipher.doFinal(encryptedData);
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
