package at.tugraz.tc.cyfile.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import at.tugraz.tc.cyfile.R;
import at.tugraz.tc.cyfile.crypto.KeyVaultService;
import at.tugraz.tc.cyfile.crypto.exceptions.KeyVaultNotInitializedException;
import at.tugraz.tc.cyfile.logging.CyFileLogger;
import at.tugraz.tc.cyfile.note.NoteService;
import at.tugraz.tc.cyfile.secret.SecretManager;
import at.tugraz.tc.cyfile.secret.SecretPrompter;
import at.tugraz.tc.cyfile.secret.impl.PinPatternSecret;

import static com.andrognito.patternlockview.PatternLockView.PatternViewMode.CORRECT;
import static com.andrognito.patternlockview.PatternLockView.PatternViewMode.WRONG;

public class PatternLockActivity extends BaseActivity {

    public static final int PATTERN_MIN_LENGTH = 3;

    private final String TAG = this.getClass().getName();

    @Inject
    SecretManager secretManager;

    @Inject
    SecretPrompter secretPrompter;

    @Inject
    KeyVaultService keyVaultService;

    @Inject
    NoteService service;

    @Inject
    CyFileLogger logger;

    private PatternLockView mPatternLockView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getActivityComponent().inject(this);
        logger.d("PatternLockActivity", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern_lock);

        mPatternLockView = findViewById(R.id.pattern_lock_view);

        PatternLockActivityListener patternListener;
        if (secretManager.secretIsSet()) {
            logger.d("PatternLockActivity", "secret is set");
            patternListener = new VerifyPatternLockListener(this);
        } else {
            logger.d("PatternLockActivity", "no secret set");
            patternListener = new PromptPatternLockListener(this);
        }

        mPatternLockView.addPatternLockListener(patternListener);
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "Back pressed");
        // prevent back
//        moveTaskToBack(true);
    }

    abstract class PatternLockActivityListener implements PatternLockViewListener {
        final Context context;

        public PatternLockActivityListener(Context context) {
            this.context = context;
        }

        @Override
        public void onStarted() {
        }

        @Override
        public void onProgress(List<PatternLockView.Dot> progressPattern) {
        }


        @Override
        public void onCleared() {
        }
    }

    class PromptPatternLockListener extends PatternLockActivityListener {
        private PinPatternSecret firstEnteredSecret;

        public PromptPatternLockListener(Context context) {
            super(context);
        }

        @Override
        public void onStarted() {
            Toast.makeText(context, getResources().getString(R.string.new_pattern_text), Toast.LENGTH_LONG).show();
        }

        //TODO no hardcoded texts
        @Override
        public void onComplete(List<PatternLockView.Dot> pattern) {
            if (pattern.size() >= PATTERN_MIN_LENGTH) {
                PinPatternSecret pinPatternSecret = new PinPatternSecret(new LinkedList<>(pattern));
                if (firstEnteredSecret == null) {
                    Toast.makeText(context, getResources().getString(R.string.confirm_pattern_text), Toast.LENGTH_LONG).show();
                    firstEnteredSecret = pinPatternSecret;
                    mPatternLockView.clearPattern();
                } else {
                    verifySecondPattern(pinPatternSecret);
                }
            } else {
                Toast.makeText(context, getResources().getString(R.string.pattern_too_short_text), Toast.LENGTH_LONG).show();
                mPatternLockView.clearPattern();
            }
        }

        private void verifySecondPattern(PinPatternSecret pinPatternSecret) {
            if (pinPatternSecret.equals(firstEnteredSecret)) {
                service.purgeRepository();
                keyVaultService.deleteKey();

                mPatternLockView.setViewMode(CORRECT);
                secretManager.setSecret(pinPatternSecret);
                String passphrase = pinPatternSecret.getSecretValue();
                keyVaultService.init(passphrase);
                keyVaultService.unlockVault(passphrase);
                finish();
            } else {
                Toast.makeText(context, getResources().getString(R.string.pattern_is_different_text), Toast.LENGTH_LONG).show();
                mPatternLockView.clearPattern();
                firstEnteredSecret = null;
            }
        }
    }

    class VerifyPatternLockListener extends PatternLockActivityListener {

        public VerifyPatternLockListener(Context context) {
            super(context);
        }

        @Override
        public void onComplete(List<PatternLockView.Dot> pattern) {
            PinPatternSecret pinPatternSecret = new PinPatternSecret(pattern);
            if (secretManager.verify(pinPatternSecret)) {
                mPatternLockView.setViewMode(CORRECT);

                try {
                    keyVaultService.unlockVault(pinPatternSecret.getSecretValue());
                } catch (KeyVaultNotInitializedException e) {
                    // in case we have a secret stored and its valid, but we didn't initialize the key vault
                    keyVaultService.init(pinPatternSecret.getSecretValue());
                    keyVaultService.unlockVault(pinPatternSecret.getSecretValue());
                }
                finish();
            } else {
                Toast.makeText(context, getResources().getString(R.string.invalid_pin), Toast.LENGTH_LONG).show();
                mPatternLockView.setViewMode(WRONG);
            }
        }
    }
}
