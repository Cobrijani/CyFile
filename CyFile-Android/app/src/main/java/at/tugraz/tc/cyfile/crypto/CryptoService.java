package at.tugraz.tc.cyfile.crypto;

/**
 * Service responsible for handling various cryptographic operations
 * Created by cobri on 3/21/2018.
 */
public interface CryptoService {

    /**
     * Perform a encryption of plain data
     *
     * @param data data represented in bytes to be encrypted
     * @return cipher text in form of byte array
     */
    byte[] encrypt(byte[] data);

    /**
     * Encrypt data and perform base64 encoding
     *
     * @param data data to be encrypted
     * @return cipher data in encoded format
     */
    String encrypt(String data);

    /**
     * Decrypt cipher data
     *
     * @param cipherData data to be decrypted
     * @return decrypted text in a form of byte array
     */
    byte[] decrypt(byte[] cipherData);

    /**
     * Decrypt data
     *
     * @param cipherData data to be decrypted
     * @return String representation of data
     */
    String decrypt(String cipherData);
}
