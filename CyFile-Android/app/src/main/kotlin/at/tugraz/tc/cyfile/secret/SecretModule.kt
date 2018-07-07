package at.tugraz.tc.cyfile.secret

import javax.inject.Singleton

import at.tugraz.tc.cyfile.crypto.KeyVaultService
import dagger.Module
import dagger.Provides

@Module
class SecretModule(private val secretManager: SecretManager, private val secretPrompter: SecretPrompter, private val keyVaultService: KeyVaultService) {

    @Provides
    @Singleton
    fun providesSecretManager(): SecretManager {
        return secretManager
    }

    @Provides
    @Singleton
    fun providesSecretPrompter(): SecretPrompter {
        return secretPrompter
    }

    @Provides
    @Singleton
    fun providesKeyVaultService(): KeyVaultService {
        return keyVaultService
    }
}
