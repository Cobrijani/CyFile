package at.tugraz.tc.cyfile.secret;


import javax.inject.Singleton;

import at.tugraz.tc.cyfile.ui.PatternLockActivity;
import dagger.Component;

@Singleton
@Component(modules = {SecretModule.class})
public interface SecretComponent {

    /**
     * Inject into activity
     *
     * @param activity activity
     */
    void inject(PatternLockActivity activity);

}
