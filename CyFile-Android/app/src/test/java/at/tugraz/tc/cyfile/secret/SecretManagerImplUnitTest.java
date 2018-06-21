package at.tugraz.tc.cyfile.secret;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SecretManagerImplUnitTest {

    private SecretManagerImpl secretManager;

    @Mock
    private SecretVerifier secretVerifier;

    @Mock
    private SecretRepository secretRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        secretManager = new SecretManagerImpl(secretVerifier, secretRepository);
    }

    @Test
    public void verifyShouldDelegateVerificationToSecretVerifier() {
        // given configured secret manager
        when(secretVerifier.verify(any()))
                .thenReturn(true);


        Secret secret = () -> "test";
        //when secret manager to verify is called
        boolean isValid = secretManager.verify(secret);

        // then verification is true and the call has been delegated to secret verifier
        Assert.assertTrue(isValid);
        verify(secretVerifier, atLeastOnce()).verify(secret);
    }

    @Test
    public void setSecretShouldSecretWhenSecretIsNull() {
        // given secret is null
        when(secretRepository.getSecret())
                .thenReturn(null);


        Secret secret = () -> "test";
        // when set secret is called
        boolean isSet = secretManager.setSecret(secret);

        // then secret manager will save the secret
        Assert.assertTrue(isSet);
        verify(secretRepository, atLeastOnce()).saveSecret(secret);
    }

    @Test
    public void setSecretShouldNotSetSecretWhenIsNotNull() {
        // given secret is set
        when(secretRepository.getSecret())
                .thenReturn(() -> "test");

        // when set secret is called
        boolean isSet = secretManager.setSecret(() -> "another");

        //then secret manager will not save secret
        verify(secretRepository, never()).saveSecret(any());
        Assert.assertFalse(isSet);
    }


}
