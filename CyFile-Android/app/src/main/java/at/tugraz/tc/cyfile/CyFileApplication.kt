package at.tugraz.tc.cyfile

import android.app.Application
import android.arch.lifecycle.ProcessLifecycleOwner
import android.content.Context
import at.tugraz.tc.cyfile.async.AsyncModule
import at.tugraz.tc.cyfile.async.impl.JobExecutor
import at.tugraz.tc.cyfile.crypto.impl.AES256CBCCryptoService
import at.tugraz.tc.cyfile.crypto.impl.KeyVaultServiceImpl
import at.tugraz.tc.cyfile.crypto.impl.NativeBase64
import at.tugraz.tc.cyfile.injection.ApplicationComponent
import at.tugraz.tc.cyfile.injection.DaggerApplicationComponent
import at.tugraz.tc.cyfile.logging.impl.AndroidLogger
import at.tugraz.tc.cyfile.note.NoteModule
import at.tugraz.tc.cyfile.note.impl.FileBasedNoteRepository
import at.tugraz.tc.cyfile.note.impl.SecureNoteService
import at.tugraz.tc.cyfile.secret.SecretModule
import at.tugraz.tc.cyfile.secret.impl.*
import com.blankj.utilcode.util.Utils

/**
 * Application extended class
 * Created by cobri on 3/21/2018.
 */

class CyFileApplication : Application() {

    // Needed to replace the component with a test specific one
    //add this prompter to the lifecycle so we which states
    var component: ApplicationComponent? = null
        get() {
            if (field == null) {

                val logger = AndroidLogger()
                val keyVaultService = KeyVaultServiceImpl(logger)

                val encoder = NativeBase64()
                val secretRepository = HashSecretRepository(this, null, logger, encoder)
                secretRepository.init()
                val prompter = OnApplicationForegroundSecretPrompter(PinPatternSecretPrompter(this), keyVaultService)

                val repository = FileBasedNoteRepository(this, null, logger)
                repository.initialize()
                ProcessLifecycleOwner.get().lifecycle.addObserver(prompter)

                val secretModule = SecretModule(
                        SecretManagerImpl(
                                HashPinPatternSecretVerifier(secretRepository),
                                secretRepository),
                        prompter,
                        keyVaultService
                )
                val cryptoService = AES256CBCCryptoService(keyVaultService, encoder, AES256CBCCryptoService.DEFAULT_ALGORITHM)
                component = DaggerApplicationComponent.builder()
                        .appModule(AppModule(this, AndroidLogger()))
                        .noteModule(NoteModule(
                                SecureNoteService(
                                        repository,
                                        cryptoService)))
                        .asyncModule(AsyncModule(JobExecutor()))
                        .secretModule(secretModule)
                        .build()
            }
            return field
        }

    override fun onCreate() {
        super.onCreate()
        Utils.init(this)
    }

    companion object {

        operator fun get(context: Context): CyFileApplication {
            return context.applicationContext as CyFileApplication
        }
    }
}
