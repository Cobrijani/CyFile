package at.tugraz.tc.cyfile.injection

import android.app.Application
import android.content.Context

import java.util.concurrent.Executor

import javax.inject.Singleton

import at.tugraz.tc.cyfile.AppModule
import at.tugraz.tc.cyfile.async.AsyncModule
import at.tugraz.tc.cyfile.crypto.CryptoModule
import at.tugraz.tc.cyfile.crypto.KeyVaultService
import at.tugraz.tc.cyfile.logging.CyFileLogger
import at.tugraz.tc.cyfile.note.NoteModule
import at.tugraz.tc.cyfile.note.NoteService
import at.tugraz.tc.cyfile.secret.SecretManager
import at.tugraz.tc.cyfile.secret.SecretModule
import at.tugraz.tc.cyfile.secret.SecretPrompter
import dagger.Component

@Singleton
@Component(modules = [(AppModule::class), (CryptoModule::class), (NoteModule::class), (SecretModule::class), (AsyncModule::class)])
interface ApplicationComponent {

    @ApplicationContext
    fun context(): Context

    fun logger(): CyFileLogger

    fun application(): Application

    fun noteService(): NoteService

    fun secretManager(): SecretManager

    fun secretPrompter(): SecretPrompter

    fun keyVaultService(): KeyVaultService

    fun executor(): Executor
}
