package at.tugraz.tc.cyfile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import at.tugraz.tc.cyfile.ui.BaseActivity;
import at.tugraz.tc.cyfile.ui.ListNoteActivity;


/**
 * Main activity
 * Created by cobri on 3/21/2018.
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActivityComponent().inject(this);

        Intent listNoteIntent = new Intent(this, ListNoteActivity.class);
        startActivity(listNoteIntent);
    }
}