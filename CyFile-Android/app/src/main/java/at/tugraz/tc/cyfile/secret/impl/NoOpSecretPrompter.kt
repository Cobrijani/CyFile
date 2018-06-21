package at.tugraz.tc.cyfile.secret.impl

import at.tugraz.tc.cyfile.secret.SecretPrompter

class NoOpSecretPrompter : SecretPrompter {
    override fun promptSecret() {}

}
