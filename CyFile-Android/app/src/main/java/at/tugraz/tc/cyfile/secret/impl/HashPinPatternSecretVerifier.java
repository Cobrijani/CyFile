package at.tugraz.tc.cyfile.secret.impl;

import at.tugraz.tc.cyfile.secret.Secret;
import at.tugraz.tc.cyfile.secret.SecretRepository;
import at.tugraz.tc.cyfile.secret.SecretVerifier;

public class HashPinPatternSecretVerifier implements SecretVerifier {
    private final SecretRepository repository;

    public HashPinPatternSecretVerifier(SecretRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean verify(Secret secret) {
        return repository.getSecret().equals(secret);
    }
}
