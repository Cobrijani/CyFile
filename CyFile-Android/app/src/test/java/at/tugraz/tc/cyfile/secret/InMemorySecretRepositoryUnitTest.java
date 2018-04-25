package at.tugraz.tc.cyfile.secret;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import at.tugraz.tc.cyfile.secret.impl.InMemorySecretRepository;

public class InMemorySecretRepositoryUnitTest {

    private InMemorySecretRepository memorySecretRepository;

    @Mock
    private Secret secret;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        memorySecretRepository = new InMemorySecretRepository(secret);

    }

    @Test
    public void getSecretShouldReturnSecretIfSecretSet() {
        // given secret is set
        memorySecretRepository = new InMemorySecretRepository(() -> "test");
        // when secret is retrieved
        Secret secret = memorySecretRepository.getSecret();

        // then secret value is correct
        Assert.assertEquals(secret.getSecretValue(), "test");
    }

    @Test
    public void getSecretShouldReturnNullIfSecretNotSet() {
        // given secret is not set
        memorySecretRepository = new InMemorySecretRepository(null);

        // when secret is retrieved
        Secret secret = memorySecretRepository.getSecret();

        // then secret value is null
        Assert.assertNull(secret);

    }

    @Test
    public void setSecretShouldSetSecretIfNotNull() {
        //given initialized state

        //when secret is being set
        boolean isSet = memorySecretRepository.saveSecret(() -> "secret");

        //then secret is saved
        Assert.assertTrue(isSet);

    }

    @Test
    public void setSecretShouldNotSetSecretIfNull() {
        //given initialized state

        //when null is being set
        boolean isSet = memorySecretRepository.saveSecret(null);

        //then secret is not saved
        Assert.assertFalse(isSet);
    }
}

