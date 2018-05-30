package at.tugraz.tc.cyfile.secret;

import org.junit.Assert;
import org.junit.Test;

import at.tugraz.tc.cyfile.BaseUnitTest;
import at.tugraz.tc.cyfile.crypto.impl.ApacheCodecBase64;
import at.tugraz.tc.cyfile.secret.impl.HashedSecret;
import at.tugraz.tc.cyfile.secret.impl.PinPatternSecret;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

public class HashedSecretUnitTest extends BaseUnitTest {

    @Test
    public void bothConstructorsShouldGiveEqualValue() {
        HashedSecret hashedSecret = new HashedSecret(() -> "111222", new ApacheCodecBase64());
        Secret pinPatternSecret = new PinPatternSecret("111222");
        Assert.assertTrue(hashedSecret.equals(pinPatternSecret));
    }

}
