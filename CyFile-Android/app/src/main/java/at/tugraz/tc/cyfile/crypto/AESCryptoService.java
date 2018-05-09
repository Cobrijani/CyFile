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
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

public class AESCryptoService implements CryptoService {
    private Cipher encCipher;
    private Cipher decCipher;
    private KeyVaultService keyVaultService;
    private byte[] currentIV;

    static final int BLOCK_SIZE = 16;
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";


    public AESCryptoService(KeyVaultService keyVaultService) {
        this.keyVaultService = keyVaultService;
    }

    public void init(String passphrase) throws InvalidKeyException {
        keyVaultService.unlockVault(passphrase);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public String encrypt(String data) throws InvalidCryptoOperationException {
        byte[] encryptedBytes = encrypt(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    private void initEncryptionCipher() throws InvalidCryptoOperationException {
        Key key = keyVaultService.getEncryptionKey();
        currentIV = new byte[BLOCK_SIZE];
        SecureRandom random = new SecureRandom();
        random.nextBytes(currentIV);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(currentIV);

        try {
            encCipher = Cipher.getInstance(ALGORITHM);
            encCipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException | InvalidKeyException e) {
            throw new InvalidCryptoOperationException(e);
        }
    }

    private void initDecryptionCipher(byte[] iv) throws InvalidCryptoOperationException {
        Key key = keyVaultService.getEncryptionKey();
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        try {
            decCipher = Cipher.getInstance(ALGORITHM);
            decCipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException | InvalidKeyException e) {
            throw new InvalidCryptoOperationException(e);
        }
    }

    @Override
    public byte[] encrypt(byte[] data) throws InvalidCryptoOperationException {
        try {
            initEncryptionCipher();
            byte[] encBytes = encCipher.doFinal(data);
            byte[] ret = new byte[encBytes.length + currentIV.length];
            System.arraycopy(currentIV, 0, ret, 0, currentIV.length);
            System.arraycopy(encBytes, 0, ret, currentIV.length, encBytes.length);
            return ret;
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new InvalidCryptoOperationException(e);
        }
    }

    @Override
    public byte[] decrypt(byte[] cipherData) throws InvalidCryptoOperationException {
        try {
            byte[] iv = new byte[BLOCK_SIZE];
            byte[] encryptedData = new byte[cipherData.length - BLOCK_SIZE];
            System.arraycopy(cipherData, 0, iv, 0, BLOCK_SIZE);
            System.arraycopy(cipherData, BLOCK_SIZE, encryptedData, 0,
                    cipherData.length - BLOCK_SIZE);
            initDecryptionCipher(iv);
            return decCipher.doFinal(encryptedData);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new InvalidCryptoOperationException(e);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String decrypt(String encryptedData) throws InvalidCryptoOperationException {
        byte[] decodedValue = Base64.getDecoder().decode(encryptedData);
        byte[] decValue = decrypt(decodedValue);
        return new String(decValue);
    }
}
