package at.tugraz.tc.cyfile.secret.impl

import at.tugraz.tc.cyfile.secret.Secret
import at.tugraz.tc.cyfile.secret.SecretRepository
import at.tugraz.tc.cyfile.secret.SecretVerifier

/**
 * [SecretVerifier] that simply checks if the string values are equal
 */
class SimplePinPatternSecretVerifier(private val secretRepository: SecretRepository) : SecretVerifier {

    override fun verify(secret: Secret): Boolean {
        val saved = secretRepository.retrieveSecret()
        return secret.secretValue == saved?.secretValue
    }

}
