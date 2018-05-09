package at.tugraz.tc.cyfile.secret.impl;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;

import at.tugraz.tc.cyfile.secret.SecretPrompter;

/**
 * Prompts {@link at.tugraz.tc.cyfile.secret.Secret}
 * whenever application is in foreground
 */
public class OnApplicationForegroundSecretPrompter implements SecretPrompter, LifecycleObserver {

    private SecretPrompter currentState;

    private SecretPrompter emptyState;

    private SecretPrompter realState;

    public OnApplicationForegroundSecretPrompter(SecretPrompter state) {
        this.emptyState = new NoOpSecretPrompter();
        this.realState = state;
        this.currentState = state;
    }

    public OnApplicationForegroundSecretPrompter(SecretPrompter emptyState, SecretPrompter realState) {
        this.emptyState = emptyState;
        this.realState = realState;
        this.currentState = realState;
    }

    @Override
    public void promptSecret() {
        currentState.promptSecret();
        currentState = emptyState;
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private void onAppBackgrounded() {
        currentState = realState;
        currentState.promptSecret();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void onAppForegrounded() {

    }


}
