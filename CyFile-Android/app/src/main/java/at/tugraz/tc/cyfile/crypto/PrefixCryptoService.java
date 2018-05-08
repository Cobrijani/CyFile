package at.tugraz.tc.cyfile.crypto;

import java.security.InvalidKeyException;

import kotlin.NotImplementedError;

/**
 * "Encrypts" text by adding a prefix.
 * this is not actual encryption, it's only purpose is for unit-testing
 */
public class PrefixCryptoService implements CryptoService {
    public static final String PREFIX = "PLEASE DON'T READ ";
    @Override
    public byte[] encrypt(byte[] data) throws InvalidCryptoOperationException {
        throw new NotImplementedError();
    }

    @Override
    public String encrypt(String data) throws InvalidCryptoOperationException {
        return PREFIX + data;
    }

    @Override
    public byte[] decrypt(byte[] cipherData) throws InvalidCryptoOperationException {
        throw new NotImplementedError();
    }

    @Override
    public String decrypt(String cipherData) throws InvalidCryptoOperationException {
        return cipherData.replaceAll(PREFIX, "");
    }

    @Override
    public void init(String passphrase) throws InvalidKeyException {

    }
}
