package at.tugraz.tc.cyfile.secret

/**
 * Repository that manages saving and retrieving [Secret] of the application
 */
interface SecretRepository {

    fun saveSecret(secret :Secret): Boolean

    fun retrieveSecret(): Secret?
}

