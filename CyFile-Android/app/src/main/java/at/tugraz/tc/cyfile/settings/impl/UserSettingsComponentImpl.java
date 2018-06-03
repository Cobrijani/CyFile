package at.tugraz.tc.cyfile.settings.impl;

import android.content.Context;
import android.content.SharedPreferences;

import at.tugraz.tc.cyfile.settings.UserSettings;
import at.tugraz.tc.cyfile.settings.UserSettingsComponent;
import at.tugraz.tc.cyfile.ui.CallReceiver;

public class UserSettingsComponentImpl implements UserSettingsComponent {
    private final Context context;
    private static final String PREFERENCES_FILE = "preferences";
    private static final String KEY_STEALTH_MODE = "settings_stealth_mode";
    private static final String KEY_MAGIC_PHONE_NUMBER = "setting_magic_phone_number";

    public UserSettingsComponentImpl(Context context) {
        this.context = context;
    }

    @Override
    public UserSettings getUserSettings() {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        boolean stealthMode = settings.getBoolean(KEY_STEALTH_MODE, false);
        String magicPhoneNumber = settings.getString(KEY_MAGIC_PHONE_NUMBER, CallReceiver.DEFAULT_MAGIC_NUMBER);
        return new UserSettings(stealthMode, magicPhoneNumber);
    }

    @Override
    public void saveUserSettings(UserSettings settings) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit();
        editor.putBoolean(KEY_STEALTH_MODE, settings.isStealthMode());
        editor.putString(KEY_MAGIC_PHONE_NUMBER, settings.getMagicPhoneNumber());
        editor.apply();
    }
}
