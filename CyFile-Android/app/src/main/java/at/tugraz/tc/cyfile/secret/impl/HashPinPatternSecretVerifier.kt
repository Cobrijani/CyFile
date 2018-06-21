package at.tugraz.tc.cyfile.secret.impl

import at.tugraz.tc.cyfile.secret.Secret
import at.tugraz.tc.cyfile.secret.SecretRepository
import at.tugraz.tc.cyfile.secret.SecretVerifier

class HashPinPatternSecretVerifier(private val repository: SecretRepository) : SecretVerifier {

    override fun verify(secret: Secret): Boolean {
        return repository.retrieveSecret() == secret
    }
}