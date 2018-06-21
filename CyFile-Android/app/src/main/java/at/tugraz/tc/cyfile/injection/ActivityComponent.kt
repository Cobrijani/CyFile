package at.tugraz.tc.cyfile.injection

import at.tugraz.tc.cyfile.MainActivity
import at.tugraz.tc.cyfile.secret.SecretPrompter
import at.tugraz.tc.cyfile.ui.DisplayNoteActivity
import at.tugraz.tc.cyfile.ui.PatternLockActivity
import dagger.Component

@PerActivity
@Component(dependencies = [(ApplicationComponent::class)])
interface ActivityComponent {
    /**
     * Inject into activity
     *
     * @param activity main activity
     */
    fun inject(activity: MainActivity)

    /**
     * Inject into activity
     *
     * @param activity
     */
    fun inject(activity: DisplayNoteActivity)

    /**
     * Inject into activity
     *
     * @param activity activity
     */
    fun inject(activity: PatternLockActivity)

    fun secretPrompter(): SecretPrompter

}
