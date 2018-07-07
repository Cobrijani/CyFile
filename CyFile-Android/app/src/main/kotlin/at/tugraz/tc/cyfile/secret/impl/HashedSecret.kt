package at.tugraz.tc.cyfile.secret.impl


import java.io.Serializable
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom

import at.tugraz.tc.cyfile.crypto.Base64
import at.tugraz.tc.cyfile.secret.Secret

class HashedSecret(secret: Secret, private val encoder: Base64) : AbstractSecret(), Serializable {
    override val secretValue: String
    private val pepper: String

    init {
        pepper = getPepper()
        this.secretValue = getHashForSecret(secret, pepper)
    }

    private fun getHashForSecret(secret: Secret, pepper: String): String {
        val secretValue = secret.secretValue + SALT + pepper
        return getSHADigest(secretValue)
    }

    private fun getPepper(): String {
        val random = SecureRandom()
        val bytes = ByteArray(PEPPER_LENGTH)
        random.nextBytes(bytes)
        return this.encoder.encode(bytes)
    }

    private fun getSHADigest(secretValue: String): String {
        val md: MessageDigest
        try {
            md = MessageDigest.getInstance("SHA-256")
        } catch (e: NoSuchAlgorithmException) {
            throw IllegalStateException(e)
        }

        md.update(secretValue.toByteArray())
        val digest = md.digest()
        return this.encoder.encode(digest)
    }

    override fun equals(other: Any?): Boolean {
        if (other is HashedSecret) {
            return other.secretValue == this.secretValue
        }

        return if (other is Secret) {
            getHashForSecret((other as Secret?)!!, this.pepper) == this.secretValue
        } else false
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + encoder.hashCode()
        result = 31 * result + secretValue.hashCode()
        result = 31 * result + pepper.hashCode()
        return result
    }

    companion object {

        const val PEPPER_LENGTH = 8
        private const val SALT = "F52FBD32B2B3B86FF88EF"
    }
}
