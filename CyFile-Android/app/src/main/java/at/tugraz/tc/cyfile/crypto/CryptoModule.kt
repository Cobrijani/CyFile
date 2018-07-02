package at.tugraz.tc.cyfile.crypto

import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class CryptoModule(private val mCryptoService: CryptoService) {

    @Provides
    @Singleton
    fun providesCryptoService(): CryptoService {
        return mCryptoService
    }

}
