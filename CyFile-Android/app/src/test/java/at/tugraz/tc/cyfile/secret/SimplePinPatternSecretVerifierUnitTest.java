package at.tugraz.tc.cyfile.secret;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SimplePinPatternSecretVerifierUnitTest {

    private SimplePinPatternSecretVerifier simplePinPatternSecretVerifier;

    @Mock
    private SecretRepository secretRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        simplePinPatternSecretVerifier = new SimplePinPatternSecretVerifier(secretRepository);
    }


    @Test
    public void verifyShouldReturnTrueIfTheValuesAreEqual() {
        when(secretRepository.getSecret())
                .thenReturn(() -> "test");

        boolean isValid = simplePinPatternSecretVerifier.verify(() -> "test");

        Assert.assertTrue(isValid);
        verify(secretRepository, atLeastOnce()).getSecret();
    }

    @Test
    public void verifyShouldReturnFalseWhenValuesAreNotEqual() {
        when(secretRepository.getSecret())
                .thenReturn(() -> "test");

        boolean isValid = simplePinPatternSecretVerifier.verify(() -> "invalidpin");

        Assert.assertFalse(isValid);
        verify(secretRepository, atLeastOnce()).getSecret();
    }

    @Test
    public void verifyShouldReturnFalseWhenNullPassed() {
        when(secretRepository.getSecret())
                .thenReturn(() -> "test");

        boolean isValid = simplePinPatternSecretVerifier.verify(null);

        Assert.assertFalse(isValid);
        verify(secretRepository, atLeastOnce()).getSecret();
    }

    @Test
    public void verifyShouldReturnFalseWhenSecretNotSet() {
        when(secretRepository.getSecret())
                .thenReturn(null);

        boolean isValid = simplePinPatternSecretVerifier.verify(() -> "test");

        Assert.assertFalse(isValid);
        verify(secretRepository, atLeastOnce()).getSecret();
    }
}
