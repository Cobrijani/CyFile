package at.tugraz.tc.cyfile.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import javax.inject.Inject;

import at.tugraz.tc.cyfile.R;
import at.tugraz.tc.cyfile.hiding.HidingComponent;
import at.tugraz.tc.cyfile.settings.UserSettings;
import at.tugraz.tc.cyfile.settings.UserSettingsComponent;

public class SettingsActivity extends BaseActivity {

    public static final String CONTENT_ID = "CONTENT_ID";
    private Switch stealthModeSwitch = null;
    private EditText phoneNmbrEditText = null;


    @Inject
    HidingComponent hidingComponent;

    @Inject
    UserSettingsComponent userSettingsComponent;

    UserSettings userSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getActivityComponent().inject(this);

        this.userSettings = userSettingsComponent.getUserSettings();
        initUiElements();
    }

    private void initUiElements() {
        phoneNmbrEditText = findViewById(R.id.settings_phone_number);
        phoneNmbrEditText.setText(userSettings.getMagicPhoneNumber());
        FloatingActionButton saveSettingsBtn = findViewById(R.id.BTN_SAVE_SETTINGS);
        saveSettingsBtn.setOnClickListener(v -> saveSettings());

        stealthModeSwitch = findViewById(R.id.hide_app_switch);
        stealthModeSwitch.setChecked(userSettings.isStealthMode());
        stealthModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                hidingComponent.hideApp(this);
            } else {
                hidingComponent.showApp(this);
            }
        });
    }

    private void saveSettings() {
        boolean stealthMode = this.stealthModeSwitch.isActivated();
        String magicPhoneNumber = this.phoneNmbrEditText.getText().toString();
        boolean numberIsOk = magicPhoneNumber.length() > 1;
        try {
            Integer.parseInt(magicPhoneNumber);
        } catch (ClassCastException ex) {
            numberIsOk = false;
        }
        if (!numberIsOk) {
            Toast.makeText(this, "Please enter a number (at least 2 digits)", Toast.LENGTH_LONG)
                    .show();
        }
        userSettingsComponent.saveUserSettings(
                new UserSettings(stealthMode, magicPhoneNumber));
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
