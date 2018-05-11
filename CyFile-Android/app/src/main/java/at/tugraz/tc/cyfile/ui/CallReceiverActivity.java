package at.tugraz.tc.cyfile.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class CallReceiverActivity extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String actionStr = intent.getAction();

        Log.d("CallReceiverActivity", actionStr + " action");

        if(actionStr.equals("android.intent.action.NEW_OUTGOING_CALL")) {

            String phoneNumber = intent.getStringExtra("android.intent.extra.PHONE_NUMBER");
            Log.d("CallReceiverActivity", "phone number dialed :- " + phoneNumber );

            if (phoneNumber.equalsIgnoreCase("12")) {
                Intent listNotesAct = new Intent(context, ListNoteActivity.class);
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

