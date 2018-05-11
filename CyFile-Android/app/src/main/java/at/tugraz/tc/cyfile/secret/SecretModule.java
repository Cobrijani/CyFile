package at.tugraz.tc.cyfile.secret;

import javax.inject.Singleton;

import at.tugraz.tc.cyfile.crypto.KeyVaultService;
import dagger.Module;
import dagger.Provides;

@Module
public class SecretModule {

    private final SecretManager secretManager;

    private final SecretPrompter secretPrompter;

    private final KeyVaultService keyVaultService;

    private final AppHidingModule appHidingModule;

    public SecretModule(SecretManager secretManager, SecretPrompter secretPrompter,
                        KeyVaultService keyVaultService, AppHidingModule appHidingModule) {
        this.secretManager = secretManager;
        this.secretPrompter = secretPrompter;
        this.keyVaultService = keyVaultService;
        this.appHidingModule = appHidingModule;
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

    @Provides
    @Singleton
    public KeyVaultService providesKeyVaultService() {
        return keyVaultService;
    }

    @Provides
    @Singleton
    public AppHidingModule providesAppHidingModule() {
        return appHidingModule;
    }
}
