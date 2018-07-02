package at.tugraz.tc.cyfile.secret.impl

import at.tugraz.tc.cyfile.secret.Secret
import at.tugraz.tc.cyfile.secret.SecretManager
import at.tugraz.tc.cyfile.secret.SecretRepository
import at.tugraz.tc.cyfile.secret.SecretVerifier

class SecretManagerImpl(private val secretVerifier: SecretVerifier, private val secretRepository: SecretRepository) : SecretManager {

    override fun secretIsSet(): Boolean {
        return secretRepository.retrieveSecret() != null
    }

    override fun verify(secret: Secret): Boolean {
        return secretVerifier.verify(secret)
    }

    override fun setSecret(secret: Secret): Boolean {
        return if (!secretIsSet()) {
            secretRepository.saveSecret(secret)
            true
        } else {
            false
        }
    }
}
