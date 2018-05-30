package at.tugraz.tc.cyfile.ui;

import android.os.Bundle;

import javax.inject.Inject;

import at.tugraz.tc.cyfile.R;
import at.tugraz.tc.cyfile.hiding.HidingComponent;

public class SettingsActivity extends BaseActivity {

    @Inject
    HidingComponent hidingComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

}
