package at.tugraz.tc.cyfile.secret.impl;

import at.tugraz.tc.cyfile.secret.Secret;
import at.tugraz.tc.cyfile.secret.SecretRepository;
import at.tugraz.tc.cyfile.secret.SecretVerifier;

public class PinPatternSecretVerifier implements SecretVerifier {

    private final SecretRepository secretRepository;

    public PinPatternSecretVerifier(SecretRepository secretRepository) {
        this.secretRepository = secretRepository;
    }

    @Override
    public boolean verify(Secret secret) {
        Secret saved = secretRepository.getSecret();


        return saved != null &&
                secret != null &&
                secret.getSecretValue().equals(saved.getSecretValue());
    }

}
