package at.tugraz.tc.cyfile.secret

interface SecretManager {

    fun secretIsSet(): Boolean

    /**
     * Verify that the secret is correct
     *
     * @param secret secret to be checked
     * @return true if valid, false otherwise
     */
    fun verify(secret: Secret): Boolean


    /**
     * Set the secret.
     *
     *
     * Can only be set if the secret is not already set
     *
     * @param secret secret to be set
     * @return true if new secret is set, false otherwise
     */
    fun setSecret(secret: Secret): Boolean
}
