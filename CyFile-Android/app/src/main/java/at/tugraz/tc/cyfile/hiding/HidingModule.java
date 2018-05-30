package at.tugraz.tc.cyfile.hiding;

import javax.inject.Singleton;

import dagger.Provides;

public class HidingModule {
    private final HidingComponent appHidingComponent;

    public HidingModule(HidingComponent appHidingComponent) {
        this.appHidingComponent = appHidingComponent;
    }

    @Provides
    @Singleton
    public HidingComponent provideHidingComponent() {
        return appHidingComponent;
    }

}
