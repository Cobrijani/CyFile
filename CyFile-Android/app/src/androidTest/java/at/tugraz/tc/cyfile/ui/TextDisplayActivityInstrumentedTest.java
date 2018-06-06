package at.tugraz.tc.cyfile.ui;

import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Rule;
import org.junit.Test;

import at.tugraz.tc.cyfile.BaseInstrumentedTest;

import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;

public class TextDisplayActivityInstrumentedTest extends BaseInstrumentedTest {
    @Rule
    public IntentsTestRule<TextDisplayActivity> testRule =
            new IntentsTestRule<>(TextDisplayActivity.class, true, false);

    @Test
    public void testBackBtn() {
        Intent intent = new Intent();
        intent.putExtra(SettingsActivity.CONTENT_ID, "");
        testRule.launchActivity(intent);
        Espresso.pressBack();
        intended(hasComponent(SettingsActivity.class.getName()));
    }
}
