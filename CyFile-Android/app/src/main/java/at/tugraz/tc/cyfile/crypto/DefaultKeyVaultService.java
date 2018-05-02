package at.tugraz.tc.cyfile.crypto;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Enumeration;

import javax.crypto.KeyGenerator;

import kotlin.NotImplementedError;

public class DefaultKeyVaultService implements KeyVaultService {
    private Key secretKey;
    private static final String KEY_ALIAS = "cyfile-encryption-key";

    @Override
    public void unlockVault(String passphrase) throws InvalidKeyException {

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

                final KeyGenParameterSpec keyGenParameterSpec = new KeyGenParameterSpec.Builder(KEY_ALIAS
                        KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                        .build();

                KeyGenerator
            }
        } catch (IOException | NoSuchAlgorithmException | CertificateException | KeyStoreException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
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
