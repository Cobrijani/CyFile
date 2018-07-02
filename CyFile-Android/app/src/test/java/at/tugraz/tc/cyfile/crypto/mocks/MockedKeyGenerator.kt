package at.tugraz.tc.cyfile.crypto.mocks

import java.security.Provider

import javax.crypto.KeyGenerator
import javax.crypto.KeyGeneratorSpi

class MockedKeyGenerator
/**
 * Creates a KeyGenerator object.
 *
 * @param keyGenSpi the delegate
 * @param provider  the provider
 * @param algorithm the algorithm
 */
(keyGenSpi: KeyGeneratorSpi, provider: Provider, algorithm: String) : KeyGenerator(keyGenSpi, provider, algorithm)
