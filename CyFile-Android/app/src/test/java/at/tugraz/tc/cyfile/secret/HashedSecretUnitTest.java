package at.tugraz.tc.cyfile.secret;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import at.tugraz.tc.cyfile.BaseUnitTest;
import at.tugraz.tc.cyfile.secret.impl.HashedSecret;
import at.tugraz.tc.cyfile.secret.impl.PinPatternSecret;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;

public class HashedSecretUnitTest extends BaseUnitTest {

    public void mockBase64(HashedSecret secret) {
        doAnswer(invocation ->
                new org.apache.commons.codec.binary.Base64().encodeToString((byte[]) invocation.getArguments()[0]))
                .when(secret)
                .encodeBase64(any(byte[].class));
    }

    @Test
    public void bothConstructorsShouldGiveEqualValue() {
        HashedSecret hashedSecret = new HashedSecret(() -> "111222");
        mockBase64(hashedSecret);
        Secret pinPatternSecret = new PinPatternSecret("111222");
        Assert.assertTrue(hashedSecret.equals(pinPatternSecret));
    }

}
