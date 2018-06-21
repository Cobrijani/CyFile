package at.tugraz.tc.cyfile

import android.app.Application
import android.content.Context

import javax.inject.Singleton

import at.tugraz.tc.cyfile.injection.ApplicationContext
import at.tugraz.tc.cyfile.logging.CyFileLogger
import dagger.Module
import dagger.Provides

/**
 * Dagger application module
 * Created by cobri on 3/21/2018.
 */

@Module
class AppModule(private val mApplication: Application, var logger: CyFileLogger) {

    @Provides
    @Singleton
    internal fun provideApplication(): Application {
        return mApplication
    }

    @Provides
    @ApplicationContext
    internal fun provideContext(): Context {
        return mApplication
    }

    @Provides
    @Singleton
    internal fun provideLogger(): CyFileLogger {
        return logger
    }

}
