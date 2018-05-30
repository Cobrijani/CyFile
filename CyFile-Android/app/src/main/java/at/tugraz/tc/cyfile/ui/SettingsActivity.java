package at.tugraz.tc.cyfile.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

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
        getActivityComponent().inject(this);

        Switch hidingSwitch = findViewById(R.id.hide_app_switch);
        hidingSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                hidingComponent.hideApp(this);
            } else {
                hidingComponent.showApp(this);
            }
        });
    }

}
