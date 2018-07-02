package at.tugraz.tc.cyfile.ui

import android.content.Intent
import android.os.Bundle
import android.widget.TextView

import at.tugraz.tc.cyfile.R

class TextDisplayActivity : BaseActivity() {

    private var content: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_display)
        aboutString = getString(R.string.about_string)
        helpString = getString(R.string.help_string)
        licensesString = getString(R.string.licenses_string)
        contactString = getString(R.string.contact_string)
        content = findViewById(R.id.text_content)
        setText()
    }

    private fun setText() {
        val contentId = intent.getStringExtra(SettingsActivity.CONTENT_ID)
        when (contentId) {
            ABOUT -> content!!.text = aboutString
            HELP -> content!!.text = helpString
            LICENSES -> content!!.text = licensesString
            CONTACT -> content!!.text = contactString
            else -> content!!.text = ""
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val i = Intent(this@TextDisplayActivity, SettingsActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(i)
        finish()
    }

    companion object {

        const val ABOUT = "ABOUT"
        const val HELP = "HELP"
        const val LICENSES = "LICENSES"
        const val CONTACT = "CONTACT"

        private var aboutString: String? = null
        private var helpString: String? = null
        private var licensesString: String? = null
        private var contactString: String? = null
    }

}
