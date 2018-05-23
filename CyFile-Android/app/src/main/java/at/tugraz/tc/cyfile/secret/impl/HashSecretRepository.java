package at.tugraz.tc.cyfile.secret.impl;


import android.content.Context;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import at.tugraz.tc.cyfile.logging.CyFileLogger;
import at.tugraz.tc.cyfile.secret.Secret;
import at.tugraz.tc.cyfile.secret.SecretRepository;

public class HashSecretRepository implements SecretRepository {
    public static final String DEFAULT_FILE_NAME = "secret";
    private HashedSecret secret;

    private final String fileName;
    private final Context context;
    private final CyFileLogger logger;

    public HashSecretRepository(Context context, String fileName, CyFileLogger logger) {
        this.fileName = fileName != null ? fileName : DEFAULT_FILE_NAME;
        this.context = context;
        this.logger = logger;
        readSecret();
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

    InputStream getInputStream() throws FileNotFoundException {
        return context.openFileInput(fileName);
    }

    OutputStream getOutputStream() throws FileNotFoundException {
        return context.openFileOutput(fileName, Context.MODE_PRIVATE);
    }

    @Override
    public Secret getSecret() {
        return secret;
    }

    @Override
    public boolean saveSecret(Secret secret) {
        this.secret = new HashedSecret(secret);
        try (ObjectOutputStream oos = new ObjectOutputStream(getOutputStream())) {
            oos.writeObject(this.secret);
            return true;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
