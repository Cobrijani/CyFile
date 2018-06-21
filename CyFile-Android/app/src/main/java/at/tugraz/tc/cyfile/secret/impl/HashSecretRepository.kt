package at.tugraz.tc.cyfile.secret.impl


import android.content.Context
import at.tugraz.tc.cyfile.crypto.Base64
import at.tugraz.tc.cyfile.logging.CyFileLogger
import at.tugraz.tc.cyfile.secret.Secret
import at.tugraz.tc.cyfile.secret.SecretRepository
import java.io.*

class HashSecretRepository(private val context: Context,
                           fileName: String?,
                           private val logger: CyFileLogger,
                           private val encoder: Base64) : SecretRepository {

    private var secret: Secret? = null
    private val fileName: String
    private var isInit = false

    val inputStream: InputStream
        @Throws(FileNotFoundException::class)
        get() = context.openFileInput(fileName)

    val outputStream: OutputStream
        @Throws(FileNotFoundException::class)
        get() = context.openFileOutput(fileName, Context.MODE_PRIVATE)

    init {
        this.fileName = fileName ?: DEFAULT_FILE_NAME
    }

    fun init() {
        readSecret()
        isInit = true
    }

    private fun readSecret() {
        try {
            ObjectInputStream(inputStream).use { ois ->
                this.secret = ois.readObject() as HashedSecret
                logger.d("HashSecretRepository", "Loaded secret")
            }
        } catch (e: FileNotFoundException) {
            logger.d("HashSecretRepository", "No secret found, is this the first run?")
        } catch (e: IOException) {
            throw IllegalStateException(e)
        } catch (e: ClassNotFoundException) {
            throw IllegalStateException(e)
        }

    }

    override fun saveSecret(secret: Secret): Boolean {
        if (this.secret != null) {
            return false
        }
        this.secret = HashedSecret(secret, encoder)
        try {
            ObjectOutputStream(outputStream).use { oos -> oos.writeObject(this.secret) }
        } catch (e: IOException) {
            throw IllegalStateException(e)
        }

        return true
    }

    override fun retrieveSecret(): Secret? {
        if (!isInit) {
            throw IllegalStateException("Must be initialized first")
        }
        return secret
    }


    companion object {
        private const val DEFAULT_FILE_NAME = "secret"
    }
}
