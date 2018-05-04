package at.tugraz.tc.cyfile.ui;

import android.os.Bundle;
import android.widget.Toast;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;

import java.security.InvalidKeyException;
import java.util.List;

import javax.inject.Inject;

import at.tugraz.tc.cyfile.R;
import at.tugraz.tc.cyfile.crypto.KeyVaultService;
import at.tugraz.tc.cyfile.secret.SecretManager;
import at.tugraz.tc.cyfile.secret.SecretPrompter;
import at.tugraz.tc.cyfile.secret.impl.PinPatternSecret;

import static com.andrognito.patternlockview.PatternLockView.PatternViewMode.CORRECT;
import static com.andrognito.patternlockview.PatternLockView.PatternViewMode.WRONG;

public class PatternLockActivity extends BaseActivity
        implements PatternLockViewListener {

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
        mPatternLockView.addPatternLockListener(this);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public void onStarted() {

    }

    @Override
    public void onProgress(List<PatternLockView.Dot> progressPattern) {

    }

    @Override
    public void onComplete(List<PatternLockView.Dot> pattern) {
        PinPatternSecret secret = new PinPatternSecret(pattern);
        if (secretManager.verify(secret)) {
            mPatternLockView.setViewMode(CORRECT);
            try {
                keyVaultService.unlockVault(secret.getSecretValue(), null);
            } catch (InvalidKeyException e) {
                e.printStackTrace();
                throw new IllegalStateException("Key was verified but rejected by the KeyVaultService");
            }
            finish();
        } else {
            Toast.makeText(this, "Invalid pin", Toast.LENGTH_LONG).show();
            mPatternLockView.setViewMode(WRONG);
        }
    }

    @Override
    public void onCleared() {

    }
}
