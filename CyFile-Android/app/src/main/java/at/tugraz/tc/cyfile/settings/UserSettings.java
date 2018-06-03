package at.tugraz.tc.cyfile.settings;

public class UserSettings {
    private final boolean stealthMode;
    private final String magicPhoneNumber;

    public boolean isStealthMode() {
        return stealthMode;
    }

    public String getMagicPhoneNumber() {
        return magicPhoneNumber;
    }

    public UserSettings(boolean stealthMode, String magicPhoneNumber) {
        this.stealthMode = stealthMode;
        this.magicPhoneNumber = magicPhoneNumber;
    }
}
