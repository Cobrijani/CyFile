package at.tugraz.tc.cyfile.secret.impl;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;

import at.tugraz.tc.cyfile.crypto.KeyVaultService;
import at.tugraz.tc.cyfile.secret.SecretPrompter;

/**
 * Prompts {@link at.tugraz.tc.cyfile.secret.Secret}
 * whenever application is in foreground
 */
public class OnApplicationForegroundSecretPrompter implements SecretPrompter, LifecycleObserver {

    private SecretPrompter currentState;

    private SecretPrompter emptyState;

    private SecretPrompter realState;

    private KeyVaultService keyVaultService;

    public OnApplicationForegroundSecretPrompter(SecretPrompter state, KeyVaultService keyVaultService) {
        this.emptyState = new NoOpSecretPrompter();
        this.realState = state;
        this.currentState = state;
        this.keyVaultService = keyVaultService;
    }

    public OnApplicationForegroundSecretPrompter(SecretPrompter emptyState, SecretPrompter realState, KeyVaultService keyVaultService) {
        this.emptyState = emptyState;
        this.realState = realState;
        this.currentState = realState;
        this.keyVaultService = keyVaultService;
    }

    @Override
    public void promptSecret() {
        currentState.promptSecret();
        currentState = emptyState;
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private void onAppBackgrounded() {
        keyVaultService.lockVault();
        currentState = realState;
        currentState.promptSecret();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void onAppForegrounded() {

    }


}
