package at.tugraz.tc.cyfile.crypto.mocks;

import java.security.Provider;

import javax.crypto.KeyGenerator;
import javax.crypto.KeyGeneratorSpi;

public class MockedKeyGenerator extends KeyGenerator {
    /**
     * Creates a KeyGenerator object.
     *
     * @param keyGenSpi the delegate
     * @param provider  the provider
     * @param algorithm the algorithm
     */
    public MockedKeyGenerator(KeyGeneratorSpi keyGenSpi, Provider provider, String algorithm) {
        super(keyGenSpi, provider, algorithm);
    }


}
