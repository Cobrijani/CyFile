package at.tugraz.tc.cyfile.ui;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import java.util.concurrent.Executor;

import at.tugraz.tc.cyfile.AppModule;
import at.tugraz.tc.cyfile.BaseInstrumentedTest;
import at.tugraz.tc.cyfile.R;
import at.tugraz.tc.cyfile.async.AsyncModule;
import at.tugraz.tc.cyfile.crypto.KeyVaultService;
import at.tugraz.tc.cyfile.injection.DaggerApplicationComponent;
import at.tugraz.tc.cyfile.logging.impl.NoOpLogger;
import at.tugraz.tc.cyfile.note.NoteModule;
import at.tugraz.tc.cyfile.note.NoteService;
import at.tugraz.tc.cyfile.secret.SecretManager;
import at.tugraz.tc.cyfile.secret.SecretModule;
import at.tugraz.tc.cyfile.secret.SecretPrompter;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Mockito.mock;

public class SettingsActivityIntrumentedTest extends BaseInstrumentedTest {

    @Mock
    private SecretManager secretManager;

    @Rule
    public IntentsTestRule<SettingsActivity> testRule =
            new IntentsTestRule<>(SettingsActivity.class, true, false);

    @Before
    public void init() {
        app.setComponent(DaggerApplicationComponent.builder()
                .appModule(new AppModule(app, new NoOpLogger()))
                .noteModule(new NoteModule(mock(NoteService.class)))
                .asyncModule(new AsyncModule(mock(Executor.class)))
                .secretModule(new SecretModule(secretManager, mock(SecretPrompter.class), mock(KeyVaultService.class))).build());

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
}
