package at.tugraz.tc.cyfile;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DisplayNoteActivityTest {
    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule =
            new IntentsTestRule<>(MainActivity.class);

    private MainActivity mainActivity;
    /**
     * Instrumented test, which will execute on an Android device.
     * <p>
     * See [testing documentation](http://d.android.com/tools/testing).
     */
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("at.tugraz.tc.cyfile", appContext.getPackageName());
    }

    public void testSaveButton() {

        onView(withId(R.id.BTN_ADD)).perform(click());
        intended(hasComponent(DisplayNoteActivity.class.getName()));


    }
}
