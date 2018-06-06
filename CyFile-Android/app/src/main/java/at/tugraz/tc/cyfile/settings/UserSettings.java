package at.tugraz.tc.cyfile.settings;

public class UserSettings {
    private boolean stealthMode;
    private String magicPhoneNumber;

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

    public void setStealthMode(boolean stealthMode) {
        this.stealthMode = stealthMode;
    }

    public void setMagicPhoneNumber(String magicPhoneNumber) {
        this.magicPhoneNumber = magicPhoneNumber;
    }
}
