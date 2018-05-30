package at.tugraz.tc.cyfile.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import javax.inject.Inject;

import at.tugraz.tc.cyfile.R;
import at.tugraz.tc.cyfile.hiding.HidingComponent;

public class SettingsActivity extends BaseActivity {

    public static final String CONTENT_ID = "CONTENT_ID";

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

    public void openHelpDialog(View view) {
        Intent intent = new Intent(this, TextDisplayActivity.class);
        intent.putExtra(CONTENT_ID, TextDisplayActivity.HELP);
        startActivity(intent);
    }

    public void openAboutDialog(View view) {
        Intent intent = new Intent(this, TextDisplayActivity.class);
        intent.putExtra(CONTENT_ID, TextDisplayActivity.ABOUT);
        startActivity(intent);
    }

    public void openLicensesDialog(View view) {
        Intent intent = new Intent(this, TextDisplayActivity.class);
        intent.putExtra(CONTENT_ID, TextDisplayActivity.LICENSES);
        startActivity(intent);
    }

    public void openContactDialog(View view) {
        Intent intent = new Intent(this, TextDisplayActivity.class);
        intent.putExtra(CONTENT_ID, TextDisplayActivity.CONTACT);
        startActivity(intent);
    }
}
