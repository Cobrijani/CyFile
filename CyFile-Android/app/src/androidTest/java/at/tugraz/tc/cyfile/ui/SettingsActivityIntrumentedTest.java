package at.tugraz.tc.cyfile.ui;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import java.util.concurrent.Executor;

import at.tugraz.tc.cyfile.AppModule;
import at.tugraz.tc.cyfile.BaseInstrumentedTest;
import at.tugraz.tc.cyfile.CyFileApplication;
import at.tugraz.tc.cyfile.MainActivity;
import at.tugraz.tc.cyfile.R;
import at.tugraz.tc.cyfile.async.AsyncModule;
import at.tugraz.tc.cyfile.crypto.KeyVaultService;
import at.tugraz.tc.cyfile.hiding.HidingComponent;
import at.tugraz.tc.cyfile.hiding.HidingModule;
import at.tugraz.tc.cyfile.injection.DaggerApplicationComponent;
import at.tugraz.tc.cyfile.logging.impl.NoOpLogger;
import at.tugraz.tc.cyfile.note.NoteModule;
import at.tugraz.tc.cyfile.note.NoteService;
import at.tugraz.tc.cyfile.secret.SecretManager;
import at.tugraz.tc.cyfile.secret.SecretModule;
import at.tugraz.tc.cyfile.secret.SecretPrompter;
import at.tugraz.tc.cyfile.secret.impl.OnApplicationForegroundSecretPrompter;
import at.tugraz.tc.cyfile.secret.impl.PinPatternSecretPrompter;
import at.tugraz.tc.cyfile.settings.SettingsModule;
import at.tugraz.tc.cyfile.settings.UserSettings;
import at.tugraz.tc.cyfile.settings.UserSettingsComponent;
import at.tugraz.tc.cyfile.settings.impl.UserSettingsComponentImpl;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class SettingsActivityIntrumentedTest extends BaseInstrumentedTest {

    @Mock
    private SecretManager secretManager;

    private UserSettings userSettings = new UserSettings(false, "");

    @Rule
    public IntentsTestRule<SettingsActivity> testRule =
            new IntentsTestRule<>(SettingsActivity.class, true, false);

    @Rule
    public IntentsTestRule<MainActivity> mainActivityRule =
            new IntentsTestRule<>(MainActivity.class, true, false);

    @Before
    public void init() {
        UserSettingsComponent userSettingsComponent = spy(new UserSettingsComponentImpl(app));
        when(userSettingsComponent.getUserSettings()).thenReturn(userSettings);

        app = (CyFileApplication)
                InstrumentationRegistry
                        .getInstrumentation()
                        .getTargetContext()
                        .getApplicationContext();
        app.setComponent(DaggerApplicationComponent.builder()
                .appModule(new AppModule(app, new NoOpLogger()))
                .noteModule(new NoteModule(mock(NoteService.class)))
                .asyncModule(new AsyncModule(mock(Executor.class)))
                .hidingModule(new HidingModule(mock(HidingComponent.class)))
                .settingsModule(new SettingsModule(userSettingsComponent))
                .secretModule(new SecretModule(mock(SecretManager.class), mock(SecretPrompter.class), mock(KeyVaultService.class))).build());
    }

    @Test
    public void settingsAboutTest() throws Exception {

        testRule.launchActivity(new Intent());

        onView(withId(R.id.settings_about)).perform(click());

        onView(withText(R.string.about_string)).check(matches(isDisplayed()));
    }

    @Test
    public void settingsHelpTest() throws Exception {

        testRule.launchActivity(new Intent());

        onView(withId(R.id.settings_help)).perform(click());

        onView(withText(R.string.help_string)).check(matches(isDisplayed()));
    }

    @Test
    public void settingsContactTest() throws Exception {

        testRule.launchActivity(new Intent());

        onView(withId(R.id.settings_contact)).perform(click());

        onView(withText(R.string.contact_string)).check(matches(isDisplayed()));
    }

    @Test
    public void settingsLicensesTest() throws Exception {

        testRule.launchActivity(new Intent());

        onView(withId(R.id.settings_licenses)).perform(click());

        onView(withText(R.string.licenses_string)).check(matches(isDisplayed()));
    }

    @Test
    public void settingsHideFeatureNotSaveTest() {
        mainActivityRule.launchActivity(new Intent());
        onView(withId(R.id.action_settings)).perform(click());

        onView(withId(R.id.hide_app_switch)).perform(click());
        allowPermissionsIfNeeded();
        onView(withId(R.id.hide_app_switch)).check(matches(isChecked()));

        pressBack();

        onView(withId(R.id.action_settings)).perform(click());

        onView(withId(R.id.hide_app_switch))
                .check(matches(not(isChecked())));
    }

    @Test
    @Ignore
    public void settingsHideFeatureSaveTest() {
        mainActivityRule.launchActivity(new Intent());
        onView(withId(R.id.action_settings)).perform(click());

        onView(withId(R.id.hide_app_switch)).perform(click());
        allowPermissionsIfNeeded();
        onView(withId(R.id.hide_app_switch)).check(matches(isChecked()));

        onView(withId(R.id.BTN_SAVE_SETTINGS)).perform(click());

        onView(withId(R.id.action_settings)).perform(click());

        onView(withId(R.id.hide_app_switch))
                .check(matches(isChecked()));
    }

    private static void allowPermissionsIfNeeded() {
        try {
            onView(withText("Allow")).perform();
        }
        catch (Exception ex)
        {}
    }
}
