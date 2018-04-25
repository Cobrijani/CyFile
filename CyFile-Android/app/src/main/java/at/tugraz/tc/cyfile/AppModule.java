package at.tugraz.tc.cyfile;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import at.tugraz.tc.cyfile.injection.ApplicationContext;
import dagger.Module;
import dagger.Provides;

/**
 * Dagger application module
 * Created by cobri on 3/21/2018.
 */

@Module
public class AppModule {

    private Application mApplication;

    public AppModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

}
