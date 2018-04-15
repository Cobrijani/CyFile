package at.tugraz.tc.cyfile.secret.impl;

import at.tugraz.tc.cyfile.secret.Secret;
import at.tugraz.tc.cyfile.secret.SecretRepository;

public class InMemorySecretRepository implements SecretRepository {

    private Secret secret;

    public InMemorySecretRepository() {
    }

    public InMemorySecretRepository(Secret secret) {
        this.secret = secret;
    }

    @Override
    public Secret getSecret() {
        return secret;
    }

    @Override
    public boolean saveSecret(Secret secret) {
        if (secret == null) {
            return false;
        } else {
            this.secret = secret;
            return true;
        }
    }
}
