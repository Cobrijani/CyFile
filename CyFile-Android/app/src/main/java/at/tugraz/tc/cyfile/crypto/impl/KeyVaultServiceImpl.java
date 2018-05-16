package at.tugraz.tc.cyfile.crypto.impl;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.KeyGenerator;

import at.tugraz.tc.cyfile.crypto.KeyVaultService;
import at.tugraz.tc.cyfile.crypto.exceptions.InvalidPassPhraseException;
import at.tugraz.tc.cyfile.crypto.exceptions.KeyVaultAlreadyInitializedException;
import at.tugraz.tc.cyfile.crypto.exceptions.KeyVaultLockedException;
import at.tugraz.tc.cyfile.crypto.exceptions.KeyVaultNotInitializedException;
import at.tugraz.tc.cyfile.crypto.exceptions.KeyVaultServiceException;

public class KeyVaultServiceImpl implements KeyVaultService {

    private Key secretKey;
    private static final String KEY_ALIAS = "cyfile-encryption-key";
    private static final String KEYSTORE_PROVIDER = "AndroidKeyStore";
    private State internalState = State.INIT;

    private KeyGenerator generator;
    private KeyStore keyStore;

    private enum State {
        INIT,
        UNLOCKED,
        LOCKED
    }

    public KeyVaultServiceImpl(KeyGenerator keyGenerator, KeyStore keyStore) {
        this.secretKey = null;
        this.keyStore = keyStore;
        this.generator = keyGenerator;
    }

    public KeyVaultServiceImpl() {
        this.secretKey = null;
        try {
            this.keyStore = KeyStore.getInstance(KEYSTORE_PROVIDER);
        } catch (KeyStoreException e) {
            throw new KeyVaultServiceException("KeyStore cannot be initialized");
        }
        try {
            generator = KeyGenerator
                    .getInstance(KeyProperties.KEY_ALGORITHM_AES, KEYSTORE_PROVIDER);
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new KeyVaultServiceException("Configuration failure");
        }

        final KeyGenParameterSpec keyGenParameterSpec = new KeyGenParameterSpec.Builder(KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setKeySize(256)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .build();

        try {
            generator.init(keyGenParameterSpec);
        } catch (InvalidAlgorithmParameterException e) {
            throw new KeyVaultServiceException("Wrong parameter configuration");
        }
    }

    @Override
    public void init(String passphrase) throws KeyVaultAlreadyInitializedException {
        if (internalState != State.INIT) {
            throw new KeyVaultAlreadyInitializedException();
        }


        secretKey = generator.generateKey();
        internalState = State.LOCKED;
    }

    @Override
    public void unlockVault(String passphrase) throws KeyVaultNotInitializedException, InvalidPassPhraseException {
        if (internalState == State.INIT) {
            throw new KeyVaultNotInitializedException("Key vault must be initialized first.");
        }

        // already unlocked
        if (internalState == State.UNLOCKED) {
            return;
        }

        try {
            this.keyStore.load(null);
            if (this.keyStore.containsAlias(KEY_ALIAS)) {
                // load key from keystore
                secretKey = this.keyStore.getKey(KEY_ALIAS, passphrase.toCharArray());
            } else {
                throw new KeyVaultServiceException("Configuration is invalid - contact programmer!");

            }
        } catch (IOException | NoSuchAlgorithmException | CertificateException | KeyStoreException | UnrecoverableKeyException e) {
            throw new InvalidPassPhraseException("Passphrase is invalid");
        }

        internalState = State.UNLOCKED;

    }

    @Override
    public void lockVault() throws KeyVaultNotInitializedException {
        if (internalState == State.LOCKED) {
            return;
        }

        if (internalState == State.INIT) {
            throw new KeyVaultNotInitializedException("Must be initialized first");
        }

        internalState = State.LOCKED;
    }

    @Override
    public Key getEncryptionKey() throws KeyVaultLockedException, KeyVaultNotInitializedException {
        if (internalState == State.INIT) {
            throw new KeyVaultNotInitializedException("Not initialized");
        }

        if (internalState == State.LOCKED) {
            throw new KeyVaultLockedException("Key vault is locked");
        }

        return secretKey;
    }
}
