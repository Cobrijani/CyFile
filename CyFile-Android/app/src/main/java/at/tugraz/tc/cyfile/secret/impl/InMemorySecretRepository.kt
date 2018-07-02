package at.tugraz.tc.cyfile.secret.impl

import at.tugraz.tc.cyfile.secret.Secret
import at.tugraz.tc.cyfile.secret.SecretRepository

class InMemorySecretRepository : SecretRepository {

    private var secret: Secret? = null

    override fun retrieveSecret(): Secret? {
        return secret
    }

    override fun saveSecret(secret: Secret): Boolean {
        return if (this.secret == null) {
            this.secret = secret
            true
        } else {
            false
        }

    }


}
