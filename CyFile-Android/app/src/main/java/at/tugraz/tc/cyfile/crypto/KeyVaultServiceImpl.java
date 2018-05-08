package at.tugraz.tc.cyfile.crypto;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.KeyGenerator;

import at.tugraz.tc.cyfile.crypto.exceptions.KeyVaultAlreadyInitializedException;
import kotlin.NotImplementedError;

public class KeyVaultServiceImpl implements KeyVaultService {

    private Key secretKey;
    private static final String KEY_ALIAS = "cyfile-encryption-key";

    public KeyVaultServiceImpl(Key key){
        this.secretKey = key;
    }

    public KeyVaultServiceImpl(){
        this.secretKey = null;
    }

    @Override
    public void init(String passphrase) throws KeyVaultAlreadyInitializedException {

    }

    @Override
    public void unlockVault(String passphrase) {

        try {
            KeyStore ks = KeyStore.getInstance("AndroidKeyStore");
            ks.load(null);
            if(ks.containsAlias(KEY_ALIAS)){

                // load key from keystore
                secretKey = ks.getKey(KEY_ALIAS, passphrase.toCharArray());
            }else{
                // otherwise generate a new key and load it
                final KeyGenerator keyGenerator = KeyGenerator
                        .getInstance(KeyProperties.KEY_ALGORITHM_AES,"AndroidKeyStore");

                final KeyGenParameterSpec keyGenParameterSpec = new KeyGenParameterSpec.Builder(KEY_ALIAS,
                        KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                        .build();


            }
        } catch (IOException | NoSuchAlgorithmException | CertificateException | KeyStoreException | UnrecoverableKeyException | NoSuchProviderException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void lockVault() {
        throw new NotImplementedError();
    }

    @Override
    public Key getEncryptionKey(){
        throw new NotImplementedError();
    }
}
