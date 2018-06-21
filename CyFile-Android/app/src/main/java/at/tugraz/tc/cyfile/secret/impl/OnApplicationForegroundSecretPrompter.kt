package at.tugraz.tc.cyfile.secret.impl

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent

import at.tugraz.tc.cyfile.crypto.KeyVaultService
import at.tugraz.tc.cyfile.crypto.exceptions.KeyVaultNotInitializedException
import at.tugraz.tc.cyfile.secret.SecretPrompter

/**
 * Prompts [at.tugraz.tc.cyfile.secret.Secret]
 * whenever application is in foreground
 */
class OnApplicationForegroundSecretPrompter(state: SecretPrompter, keyVaultService: KeyVaultService) : SecretPrompter, LifecycleObserver {

    private var currentState: SecretPrompter? = state

    private var emptyState: SecretPrompter? = null

    private var realState: SecretPrompter? = null

    private var keyVaultService: KeyVaultService? = keyVaultService

    override fun promptSecret() {
        currentState!!.promptSecret()
        currentState = emptyState
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun onAppBackgrounded() {
        try {
            keyVaultService!!.lockVault()
        } catch (e: KeyVaultNotInitializedException) {
            e.printStackTrace()
        }

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun onAppForegrounded() {
        currentState = realState
        currentState!!.promptSecret()

    }

    init {
        this.emptyState = NoOpSecretPrompter()
    }


}
