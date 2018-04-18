package at.tugraz.tc.cyfile.secret.impl;

import at.tugraz.tc.cyfile.secret.Secret;
import at.tugraz.tc.cyfile.secret.SecretRepository;
import at.tugraz.tc.cyfile.secret.SecretVerifier;

/**
 * {@link SecretVerifier} that simply checks if the string values are equal
 */
public class SimplePinPatternSecretVerifier implements SecretVerifier {

    private final SecretRepository secretRepository;

    public SimplePinPatternSecretVerifier(SecretRepository secretRepository) {
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
