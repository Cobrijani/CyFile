package at.tugraz.tc.cyfile.crypto;

/**
 * Implementation of {@link CryptoService}
 * Created by cobri on 3/21/2018.
 */

public class NoOpCryptoService implements CryptoService {
    @Override
    public byte[] encrypt(byte[] data) {
        return data;
    }

    @Override
    public byte[] decrypt(byte[] cipherData) {
        return cipherData;
    }

    @Override
    public String encrypt(String data) {
        return data;
    }

    @Override
    public String decrypt(String cipherData) {
        return cipherData;
    }
}
