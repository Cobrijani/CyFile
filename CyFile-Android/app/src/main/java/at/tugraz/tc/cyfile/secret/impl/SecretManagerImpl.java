package at.tugraz.tc.cyfile.secret.impl;

import at.tugraz.tc.cyfile.secret.Secret;
import at.tugraz.tc.cyfile.secret.SecretManager;
import at.tugraz.tc.cyfile.secret.SecretRepository;
import at.tugraz.tc.cyfile.secret.SecretVerifier;

public class SecretManagerImpl implements SecretManager {

    private final SecretVerifier secretVerifier;

    private final SecretRepository secretRepository;

    public SecretManagerImpl(SecretVerifier secretVerifier, SecretRepository secretRepository) {
        this.secretVerifier = secretVerifier;
        this.secretRepository = secretRepository;
    }

    @Override
    public boolean secretIsSet() {
        return secretRepository.getSecret() != null;
    }

    @Override
    public boolean verify(Secret secret) {
        return secretVerifier.verify(secret);
    }

    @Override
    public boolean setSecret(Secret secret) {
        if (secretRepository.getSecret() == null) {
            secretRepository.saveSecret(secret);
            return true;
        } else {
            return false;
        }
    }
}
