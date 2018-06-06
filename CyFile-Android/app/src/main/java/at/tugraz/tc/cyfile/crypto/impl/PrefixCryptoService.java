package at.tugraz.tc.cyfile.crypto.impl;

import at.tugraz.tc.cyfile.crypto.CryptoService;
import kotlin.NotImplementedError;

/**
 * "Encrypts" text by adding a prefix.
 * this is not actual encryption, it's only purpose is for unit-testing
 */
public class PrefixCryptoService implements CryptoService {
    public static final String PREFIX = "PLEASE DON'T READ ";
    @Override
    public byte[] encrypt(byte[] data) {
        throw new NotImplementedError();
    }

    @Override
    public String encrypt(String data) {
        return PREFIX + data;
    }

    @Override
    public byte[] decrypt(byte[] cipherData) {
        throw new NotImplementedError();
    }

    @Override
    public String decrypt(String cipherData) {
        return cipherData.replaceAll(PREFIX, "");
    }

}
