package at.tugraz.tc.cyfile.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
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

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

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
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.PROCESS_OUTGOING_CALLS)
                        == PackageManager.PERMISSION_GRANTED) {
                    hidingComponent.hideApp(this);

                } else if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.PROCESS_OUTGOING_CALLS)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.PROCESS_OUTGOING_CALLS},
                            MY_PERMISSIONS_REQUEST_CALL_PHONE);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                hidingComponent.showApp(this);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    hidingComponent.hideApp(this);
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private void saveSettings() {
        boolean stealthMode = this.stealthModeSwitch.isChecked();
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
