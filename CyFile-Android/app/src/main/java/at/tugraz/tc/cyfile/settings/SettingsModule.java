package at.tugraz.tc.cyfile.settings;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class HidingModule {
    private final SettingsComponent appHidingComponent;

    public HidingModule(SettingsComponent appHidingComponent) {
        this.appHidingComponent = appHidingComponent;
    }

    @Provides
    @Singleton
    public SettingsComponent provideHidingComponent() {
        return appHidingComponent;
    }

}
