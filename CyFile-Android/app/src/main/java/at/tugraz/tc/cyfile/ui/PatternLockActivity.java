package at.tugraz.tc.cyfile.ui;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;

import java.util.List;

import javax.inject.Inject;

import at.tugraz.tc.cyfile.R;
import at.tugraz.tc.cyfile.crypto.KeyVaultService;
import at.tugraz.tc.cyfile.secret.SecretManager;
import at.tugraz.tc.cyfile.secret.SecretPrompter;
import at.tugraz.tc.cyfile.secret.impl.PinPatternSecret;

import static com.andrognito.patternlockview.PatternLockView.PatternViewMode.CORRECT;
import static com.andrognito.patternlockview.PatternLockView.PatternViewMode.WRONG;

public class PatternLockActivity extends BaseActivity {

    public static final int PATTERN_MIN_LENGTH = 3;

    @Inject
    SecretManager secretManager;

    @Inject
    SecretPrompter secretPrompter;

    @Inject
    KeyVaultService keyVaultService;

    private PatternLockView mPatternLockView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern_lock);

        getActivityComponent().inject(this);

        mPatternLockView = findViewById(R.id.pattern_lock_view);

        PatternLockActivityListener patternListener;
        if (secretManager.secretIsSet()) {
            patternListener = new VerifyPatternLockListener(this);
        } else {
            patternListener = new PromptPatternLockListener(this);
        }

        mPatternLockView.addPatternLockListener(patternListener);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
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
            Toast.makeText(context, "Enter new key", Toast.LENGTH_LONG).show();
            super.onStarted();
        }

        //TODO no hardcoded texts
        @Override
        public void onComplete(List<PatternLockView.Dot> pattern) {
            if (pattern.size() >= PATTERN_MIN_LENGTH) {
                PinPatternSecret pinPatternSecret = new PinPatternSecret(pattern);
                if (firstEnteredSecret == null) {
                    Toast.makeText(context, "Please confirm", Toast.LENGTH_LONG).show();
                    firstEnteredSecret = pinPatternSecret;
                    mPatternLockView.clearPattern();
                } else {
                    verifySecondPattern(pinPatternSecret);
                }
            } else {
                Toast.makeText(context, "Pattern too short...", Toast.LENGTH_LONG).show();
                mPatternLockView.clearPattern();
            }
        }

        private void verifySecondPattern(PinPatternSecret pinPatternSecret) {
            if (pinPatternSecret.equals(firstEnteredSecret)) {
                mPatternLockView.setViewMode(CORRECT);
                secretManager.setSecret(pinPatternSecret);
                String passphrase = pinPatternSecret.getSecretValue();
                keyVaultService.init(passphrase);
                keyVaultService.unlockVault(passphrase);
                finish();
            } else {
                Toast.makeText(context, "Different", Toast.LENGTH_LONG).show();
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
                keyVaultService.unlockVault(pinPatternSecret.getSecretValue());
                finish();
            } else {
                Toast.makeText(context, "Invalid pin", Toast.LENGTH_LONG).show();
                mPatternLockView.setViewMode(WRONG);
            }
        }
    }
}
