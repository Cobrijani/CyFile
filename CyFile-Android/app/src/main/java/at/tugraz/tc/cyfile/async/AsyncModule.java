package at.tugraz.tc.cyfile.async;


import java.util.concurrent.Executor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AsyncModule {

    private final Executor executor;

    public AsyncModule(Executor executor) {
        this.executor = executor;
    }

    @Provides
    @Singleton
    public Executor providesExecutor() {
        return executor;
    }
}
