package at.tugraz.tc.cyfile.secret.impl;


import android.content.Context;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import at.tugraz.tc.cyfile.crypto.Base64;
import at.tugraz.tc.cyfile.logging.CyFileLogger;
import at.tugraz.tc.cyfile.secret.Secret;
import at.tugraz.tc.cyfile.secret.SecretRepository;

public class HashSecretRepository implements SecretRepository {
    public static final String DEFAULT_FILE_NAME = "secret";
    private HashedSecret secret;

    private final String fileName;
    private final Context context;
    private final CyFileLogger logger;
    private final Base64 encoder;
    private boolean isInit = false;

    public HashSecretRepository(Context context, String fileName, CyFileLogger logger, Base64 encoder) {
        this.fileName = fileName != null ? fileName : DEFAULT_FILE_NAME;
        this.context = context;
        this.logger = logger;
        this.encoder = encoder;
    }

    public void init() {
        readSecret();
        isInit = true;
    }

    private void readSecret() {
        try (ObjectInputStream ois = new ObjectInputStream(getInputStream())) {
            this.secret = (HashedSecret) ois.readObject();
            logger.d("HashSecretRepository", "Loaded secret");
        } catch (FileNotFoundException e) {
            logger.d("HashSecretRepository", "No secret found, is this the first run?");
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    public InputStream getInputStream() throws FileNotFoundException {
        return context.openFileInput(fileName);
    }

    public OutputStream getOutputStream() throws FileNotFoundException {
        return context.openFileOutput(fileName, Context.MODE_PRIVATE);
    }

    @Override
    public Secret getSecret() {
        if (!isInit) {
            throw new IllegalStateException("Must be initialized first");
        }
        return secret;
    }

    @Override
    public boolean saveSecret(Secret secret) {
        this.secret = new HashedSecret(secret, encoder);
        try (ObjectOutputStream oos = new ObjectOutputStream(getOutputStream())) {
            oos.writeObject(this.secret);
            return true;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
