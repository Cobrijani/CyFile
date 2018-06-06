package at.tugraz.tc.cyfile.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import javax.inject.Inject;

import at.tugraz.tc.cyfile.CyFileApplication;
import at.tugraz.tc.cyfile.injection.ActivityComponent;
import at.tugraz.tc.cyfile.injection.DaggerActivityComponent;
import at.tugraz.tc.cyfile.settings.UserSettings;
import at.tugraz.tc.cyfile.settings.UserSettingsComponent;

public class CallReceiver extends BroadcastReceiver {
    public static final String DEFAULT_MAGIC_NUMBER = "1234";

    private ActivityComponent mActivityComponent;

    @Inject
    UserSettingsComponent settingsComponent;

    private ActivityComponent getActivityComponent(Context context) {
        if (mActivityComponent == null) {
            mActivityComponent = DaggerActivityComponent.builder()
                    .applicationComponent(CyFileApplication.get(context).getComponent())
                    .build();
        }
        return mActivityComponent;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        getActivityComponent(context).inject(this);
        String actionStr = intent.getAction();

        Log.d("CallReceiver", actionStr + " action");

        if("android.intent.action.NEW_OUTGOING_CALL".equals(actionStr)) {

            String phoneNumber = intent.getStringExtra("android.intent.extra.PHONE_NUMBER");

            UserSettings userSettings = settingsComponent.getUserSettings();

            String magicPhoneNumber = userSettings.getMagicPhoneNumber();
            if (userSettings.isStealthMode()
                    && phoneNumber.equalsIgnoreCase(magicPhoneNumber)) {
                Log.d("CallReceiver", "magic phone number dialed");
                Intent listNotesAct = new Intent(context, NoteListActivity.class);
                listNotesAct.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(listNotesAct);

                //Cancel outgoing call
                setResultData(null);
                abortBroadcast();
                // cancel the call
            }

        }
    }
}

