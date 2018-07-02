package at.tugraz.tc.cyfile.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import at.tugraz.tc.cyfile.R
import at.tugraz.tc.cyfile.crypto.KeyVaultService
import at.tugraz.tc.cyfile.crypto.exceptions.KeyVaultNotInitializedException
import at.tugraz.tc.cyfile.logging.CyFileLogger
import at.tugraz.tc.cyfile.secret.SecretManager
import at.tugraz.tc.cyfile.secret.SecretPrompter
import at.tugraz.tc.cyfile.secret.impl.PinPatternSecret
import com.andrognito.patternlockview.PatternLockView
import com.andrognito.patternlockview.PatternLockView.PatternViewMode.CORRECT
import com.andrognito.patternlockview.PatternLockView.PatternViewMode.WRONG
import com.andrognito.patternlockview.listener.PatternLockViewListener
import java.util.*
import javax.inject.Inject

class PatternLockActivity : BaseActivity() {

    private val tag = this.javaClass.name

    @Inject
    lateinit var secretManager: SecretManager

    @Inject
    lateinit var secretPrompter: SecretPrompter

    @Inject
    lateinit var keyVaultService: KeyVaultService

    @Inject
    lateinit var logger: CyFileLogger

    private var mPatternLockView: PatternLockView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent.inject(this)
        logger.d("PatternLockActivity", "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pattern_lock)

        mPatternLockView = findViewById(R.id.pattern_lock_view)

        val patternListener: PatternLockActivityListener = if (secretManager.secretIsSet()) {
            logger.d("PatternLockActivity", "secret is set")
            VerifyPatternLockListener(this)
        } else {
            logger.d("PatternLockActivity", "no secret set")
            PromptPatternLockListener(this)
        }

        mPatternLockView!!.addPatternLockListener(patternListener)
    }

    override fun onBackPressed() {
        Log.d(tag, "Back pressed")
        // prevent back
        //        moveTaskToBack(true);
    }

    internal abstract inner class PatternLockActivityListener(val context: Context) : PatternLockViewListener {

        override fun onStarted() {}

        override fun onProgress(progressPattern: List<PatternLockView.Dot>) {}


        override fun onCleared() {}
    }

    internal inner class PromptPatternLockListener(context: Context) : PatternLockActivityListener(context) {
        private var firstEnteredSecret: PinPatternSecret? = null

        override fun onStarted() {
            Toast.makeText(context, "Please enter a new pattern", Toast.LENGTH_LONG).show()
        }

        //TODO no hardcoded texts
        override fun onComplete(pattern: List<PatternLockView.Dot>) {
            if (pattern.size >= PATTERN_MIN_LENGTH) {
                val pinPatternSecret = PinPatternSecret(LinkedList<PatternLockView.Dot>(pattern))
                if (firstEnteredSecret == null) {
                    Toast.makeText(context, "Please confirm", Toast.LENGTH_LONG).show()
                    firstEnteredSecret = pinPatternSecret
                    mPatternLockView!!.clearPattern()
                } else {
                    verifySecondPattern(pinPatternSecret)
                }
            } else {
                Toast.makeText(context, "Pattern too short...", Toast.LENGTH_LONG).show()
                mPatternLockView!!.clearPattern()
            }
        }

        private fun verifySecondPattern(pinPatternSecret: PinPatternSecret) {
            if (pinPatternSecret == firstEnteredSecret) {
                mPatternLockView!!.setViewMode(CORRECT)
                secretManager.setSecret(pinPatternSecret)
                val passphrase = pinPatternSecret.secretValue
                keyVaultService.init(passphrase)
                keyVaultService.unlockVault(passphrase)
                finish()
            } else {
                Toast.makeText(context, "Different", Toast.LENGTH_LONG).show()
                mPatternLockView!!.clearPattern()
                firstEnteredSecret = null
            }
        }
    }

    internal inner class VerifyPatternLockListener(context: Context) : PatternLockActivityListener(context) {

        override fun onComplete(pattern: List<PatternLockView.Dot>) {
            val pinPatternSecret = PinPatternSecret(pattern)
            if (secretManager.verify(pinPatternSecret)) {
                mPatternLockView!!.setViewMode(CORRECT)

                try {
                    keyVaultService.unlockVault(pinPatternSecret.secretValue)
                } catch (e: KeyVaultNotInitializedException) {
                    // in case we have a secret stored and its valid, but we didn't initialize the key vault
                    keyVaultService.init(pinPatternSecret.secretValue)
                    keyVaultService.unlockVault(pinPatternSecret.secretValue)
                }

                finish()
            } else {
                Toast.makeText(context, "Invalid pin", Toast.LENGTH_LONG).show()
                mPatternLockView!!.setViewMode(WRONG)
            }
        }
    }

    companion object {
        const val PATTERN_MIN_LENGTH = 3
    }
}
