package at.tugraz.tc.cyfile.secret.impl

import at.tugraz.tc.cyfile.crypto.Base64
import at.tugraz.tc.cyfile.file.FileHandler
import at.tugraz.tc.cyfile.logging.CyFileLogger
import at.tugraz.tc.cyfile.secret.Secret
import at.tugraz.tc.cyfile.secret.SecretRepository
import java.io.*

class HashSecretRepository(private val fileHandler: FileHandler, private val logger: CyFileLogger, private val encoder: Base64)
    : SecretRepository {

    private var secret: Secret? = null


    init {
        readSecret()
    }

    private fun readSecret() {
        try {
            ObjectInputStream(fileHandler.getInputStream()).use { ois ->
                this.secret = ois.readObject() as HashedSecret
                logger.d("HashSecretRepository", "Loaded secret")
            }
        } catch (e: FileNotFoundException) {
            logger.d("HashSecretRepository", "No secret found, is this the first run?")
        } catch (e: EOFException){
            logger.d("HashSecretRepository", "Secret file is empty, no secret")
            this.secret = null
        }catch (e: IOException) {
            throw IllegalStateException(e)
        } catch (e: ClassNotFoundException) {
            throw IllegalStateException(e)
        }

    }

    override fun saveSecret(secret: Secret): Boolean {
        this.secret = HashedSecret(secret, encoder)
        try {
            ObjectOutputStream(fileHandler.getOutputStream())
                    .use { oos -> oos.writeObject(this.secret) }
        } catch (e: IOException) {
            throw IllegalStateException(e)
        }

        return true
    }

    override fun retrieveSecret(): Secret? {
        return secret
    }


    companion object {
        const val DEFAULT_FILE_NAME = "secret"
    }
}
