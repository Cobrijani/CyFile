package at.tugraz.tc.cyfile.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;

import java.util.List;

import javax.inject.Inject;

import at.tugraz.tc.cyfile.CyFileApplication;
import at.tugraz.tc.cyfile.R;
import at.tugraz.tc.cyfile.secret.SecretManager;
import at.tugraz.tc.cyfile.secret.impl.PinPatternSecret;

import static com.andrognito.patternlockview.PatternLockView.PatternViewMode.CORRECT;
import static com.andrognito.patternlockview.PatternLockView.PatternViewMode.WRONG;

public class PatternLockActivity extends AppCompatActivity
        implements PatternLockViewListener {

    @Inject
    SecretManager secretManager;

    private PatternLockView mPatternLockView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern_lock);

        ((CyFileApplication) getApplication()).getSecretVerifierComponent().inject(this);
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
        if (secretManager.verify(new PinPatternSecret(pattern))) {
            mPatternLockView.setViewMode(CORRECT);
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
