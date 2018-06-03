package at.tugraz.tc.cyfile.settings;

public interface UserSettingsComponent {
    UserSettings getUserSettings();
    void saveUserSettings(UserSettings settings);
}
