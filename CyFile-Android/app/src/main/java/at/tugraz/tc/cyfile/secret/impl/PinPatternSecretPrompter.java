package at.tugraz.tc.cyfile.secret.impl;

import android.content.Context;
import android.content.Intent;

import at.tugraz.tc.cyfile.secret.SecretPrompter;
import at.tugraz.tc.cyfile.ui.PatternLockActivity;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

public class PinPatternSecretPrompter implements SecretPrompter {

    private Context context;

    public PinPatternSecretPrompter(Context context) {
        this.context = context;
    }

    @Override
    public void promptSecret() {
        Intent intent = new Intent(context, PatternLockActivity.class);
        startActivity(intent);
    }


}
