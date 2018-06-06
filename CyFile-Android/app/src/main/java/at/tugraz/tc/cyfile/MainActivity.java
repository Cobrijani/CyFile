package at.tugraz.tc.cyfile;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;

import at.tugraz.tc.cyfile.ui.BaseActivity;
import at.tugraz.tc.cyfile.ui.CallReceiver;
import at.tugraz.tc.cyfile.ui.NoteListActivity;


public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, NoteListActivity.class);
        startActivity(intent);
        this.registerReceiver(new CallReceiver(), new IntentFilter("android.intent.action.NEW_OUTGOING_CALL"));
    }
}
