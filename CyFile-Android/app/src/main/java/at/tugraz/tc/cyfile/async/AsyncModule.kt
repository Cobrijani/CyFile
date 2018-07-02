package at.tugraz.tc.cyfile.async

import dagger.Module
import dagger.Provides
import java.util.concurrent.Executor
import javax.inject.Singleton

@Module
class AsyncModule(val executor: Executor) {


    @Provides
    @Singleton
    fun providesExecutor(): Executor {
        return executor
    }
}