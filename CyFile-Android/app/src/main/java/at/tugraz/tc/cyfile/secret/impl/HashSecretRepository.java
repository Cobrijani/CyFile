package at.tugraz.tc.cyfile.secret.impl;


import android.content.Context;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import at.tugraz.tc.cyfile.secret.Secret;
import at.tugraz.tc.cyfile.secret.SecretRepository;

public class HashSecretRepository implements SecretRepository {
    private HashedSecret secret;

    private final String fileName;
    private final Context context;

    public HashSecretRepository(Context context, String fileName) {
        this.fileName = fileName != null ? fileName : HashPinPatternSecretVerifier.DEFAULT_FILE_NAME;
        this.context = context;
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
        String digest = this.secret.getSecretValue();
        try (ObjectOutputStream oos = new ObjectOutputStream(getOutputStream())) {
            oos.writeObject(digest);
            return true;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        //TODO make check if file exists
    }
}