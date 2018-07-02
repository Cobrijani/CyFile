package at.tugraz.tc.cyfile.ui

import android.content.Intent
import android.support.test.espresso.Espresso
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent
import android.support.test.espresso.intent.rule.IntentsTestRule
import at.tugraz.tc.cyfile.BaseInstrumentedTest
import org.junit.Rule
import org.junit.Test

class TextDisplayActivityInstrumentedTest : BaseInstrumentedTest() {
    @Rule
    @JvmField
    var testRule = IntentsTestRule(TextDisplayActivity::class.java, true, false)

    @Test
    fun testBackBtn() {
        val intent = Intent()
        intent.putExtra(SettingsActivity.CONTENT_ID, "")
        testRule.launchActivity(intent)
        Espresso.pressBack()
        intended(hasComponent(SettingsActivity::class.java.name))
    }
}
