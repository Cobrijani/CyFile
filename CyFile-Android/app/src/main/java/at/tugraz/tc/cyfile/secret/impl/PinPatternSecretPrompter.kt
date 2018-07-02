package at.tugraz.tc.cyfile.secret.impl

import android.content.Context
import android.content.Intent

import at.tugraz.tc.cyfile.secret.SecretPrompter
import at.tugraz.tc.cyfile.ui.PatternLockActivity

import com.blankj.utilcode.util.ActivityUtils.startActivity

class PinPatternSecretPrompter(private val context: Context) : SecretPrompter {

    override fun promptSecret() {
        val intent = Intent(context, PatternLockActivity::class.java)
        startActivity(intent)
    }


}
