package at.tugraz.tc.cyfile.injection;

import at.tugraz.tc.cyfile.MainActivity;
import at.tugraz.tc.cyfile.secret.SecretPrompter;
import at.tugraz.tc.cyfile.ui.DisplayNoteActivity;
import at.tugraz.tc.cyfile.ui.PatternLockActivity;
import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class)
public interface ActivityComponent {
    /**
     * Inject into activity
     *
     * @param activity main activity
     */
    void inject(MainActivity activity);

    /**
     * Inject into activity
     *
     * @param activity
     */
    void inject(DisplayNoteActivity activity);

    /**
     * Inject into activity
     *
     * @param activity activity
     */
    void inject(PatternLockActivity activity);

    SecretPrompter secretPrompter();

}
