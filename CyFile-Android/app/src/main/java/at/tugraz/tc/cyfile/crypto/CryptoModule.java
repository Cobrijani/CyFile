package at.tugraz.tc.cyfile.crypto;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module for crypto
 * Created by cobri on 3/21/2018.
 */

@Module
public class CryptoModule {

    private final CryptoService mCryptoService;

    public CryptoModule(CryptoService mCryptoService) {
        this.mCryptoService = mCryptoService;
    }

    @Provides
    @Singleton
    public CryptoService providesCryptoService() {
        return mCryptoService;
    }

}
