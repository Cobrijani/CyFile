package at.tugraz.tc.cyfile.ui

import android.content.Intent
import android.os.Bundle
import android.view.View

import at.tugraz.tc.cyfile.R

class SettingsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }

    fun openHelpDialog(view: View) {
        val intent = Intent(this, TextDisplayActivity::class.java)
        intent.putExtra(CONTENT_ID, TextDisplayActivity.HELP)
        startActivity(intent)
    }

    fun openAboutDialog(view: View) {
        val intent = Intent(this, TextDisplayActivity::class.java)
        intent.putExtra(CONTENT_ID, TextDisplayActivity.ABOUT)
        startActivity(intent)
    }

    fun openLicensesDialog(view: View) {
        val intent = Intent(this, TextDisplayActivity::class.java)
        intent.putExtra(CONTENT_ID, TextDisplayActivity.LICENSES)
        startActivity(intent)
    }

    fun openContactDialog(view: View) {
        val intent = Intent(this, TextDisplayActivity::class.java)
        intent.putExtra(CONTENT_ID, TextDisplayActivity.CONTACT)
        startActivity(intent)
    }

    companion object {
        const val CONTENT_ID = "CONTENT_ID"
    }
}
