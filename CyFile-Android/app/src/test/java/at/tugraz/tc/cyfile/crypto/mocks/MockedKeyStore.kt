package at.tugraz.tc.cyfile.crypto.mocks

import java.security.KeyStore
import java.security.KeyStoreSpi
import java.security.Provider

class MockedKeyStore
/**
 * Creates a KeyStore object of the given type, and encapsulates the given
 * provider implementation (SPI object) in it.
 *
 * @param keyStoreSpi the provider implementation.
 * @param provider    the provider.
 * @param type        the keystore type.
 */
(keyStoreSpi: KeyStoreSpi, provider: Provider, type: String) : KeyStore(keyStoreSpi, provider, type)
