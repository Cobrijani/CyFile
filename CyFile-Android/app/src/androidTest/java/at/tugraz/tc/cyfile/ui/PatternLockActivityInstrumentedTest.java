package at.tugraz.tc.cyfile.ui;

import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import at.tugraz.tc.cyfile.CyFileApplication;
import at.tugraz.tc.cyfile.MainActivity;
import at.tugraz.tc.cyfile.R;
import at.tugraz.tc.cyfile.secret.DaggerSecretComponent;
import at.tugraz.tc.cyfile.secret.SecretManager;
import at.tugraz.tc.cyfile.secret.SecretModule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class PatternLockActivityInstrumentedTest {


    @Rule
    public ActivityTestRule<PatternLockActivity> activityTestRule = new ActivityTestRule<>(PatternLockActivity.class,
            true,
            false);

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class,
            true,
            false);

    @Mock
    SecretManager secretManager;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        CyFileApplication app
                = (CyFileApplication) instrumentation.getTargetContext().getApplicationContext();

        app
                .setSecretVerifierComponent(DaggerSecretComponent.builder()
                        .secretModule(new SecretModule(secretManager)).build());
    }

    @Test
    public void correctPatternRemovesPatternView() {
        when(secretManager.verify(any()))
                .thenReturn(true);

        mainActivityActivityTestRule.launchActivity(new Intent());

        onView(withId(R.id.pattern_lock_view))
                .perform(ViewActions.swipeDown());

        onView(withId(R.id.pattern_lock_view)).check(doesNotExist());

    }

    @Test
    public void falsePatternDoesNotRemoveView() {
        when(secretManager.verify(any()))
                .thenReturn(false);

        mainActivityActivityTestRule.launchActivity(new Intent());

        onView(withId(R.id.pattern_lock_view))
                .perform(ViewActions.swipeDown());

        onView(withId(R.id.pattern_lock_view)).check(matches(isDisplayed()));
    }
}
