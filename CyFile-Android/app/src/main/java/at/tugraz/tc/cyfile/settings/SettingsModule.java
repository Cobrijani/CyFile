package at.tugraz.tc.cyfile.settings;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class SettingsModule {
    private final UserSettingsComponent userSettings;

    public SettingsModule(UserSettingsComponent appHidingComponent) {
        this.userSettings = appHidingComponent;
    }

    @Provides
    @Singleton
    public UserSettingsComponent provideUserSettings() {
        return userSettings;
    }

}
