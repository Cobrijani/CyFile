package at.tugraz.tc.cyfile.secret;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class SecretModule {


    private SecretManager secretManager;

    public SecretModule(SecretManager secretManager) {
        this.secretManager = secretManager;
    }

    @Provides
    @Singleton
    public SecretManager providesSecretManager() {
        return secretManager;
    }
}
