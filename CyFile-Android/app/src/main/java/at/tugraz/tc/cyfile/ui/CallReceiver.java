package at.tugraz.tc.cyfile.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class CallReceiver extends BroadcastReceiver {
    public static final String DEFAULT_MAGIC_NUMBER = "1234";
    @Override
    public void onReceive(Context context, Intent intent) {

        String actionStr = intent.getAction();

        Log.d("CallReceiver", actionStr + " action");

        if("android.intent.action.NEW_OUTGOING_CALL".equals(actionStr)) {

            String phoneNumber = intent.getStringExtra("android.intent.extra.PHONE_NUMBER");
            Log.d("CallReceiver", "phone number dialed :- " + phoneNumber );

            if (phoneNumber.equalsIgnoreCase("12")) {
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

