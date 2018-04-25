package at.tugraz.tc.cyfile.secret;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class SecretModule {

    private final SecretManager secretManager;

    private final SecretPrompter secretPrompter;

    public SecretModule(SecretManager secretManager, SecretPrompter secretPrompter) {
        this.secretManager = secretManager;
        this.secretPrompter = secretPrompter;
    }

    @Provides
    @Singleton
    public SecretManager providesSecretManager() {
        return secretManager;
    }

    @Provides
    @Singleton
    public SecretPrompter providesSecretPrompter() {
        return secretPrompter;
    }
}
