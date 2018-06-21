package at.tugraz.tc.cyfile.secret;

import org.junit.Assert;
import org.junit.Test;

public class HashedSecretUnitTest extends BaseUnitTest {

    @Test
    public void bothConstructorsShouldGiveEqualValue() {
        HashedSecret hashedSecret = new HashedSecret(() -> "111222", new ApacheCodecBase64());
        Secret pinPatternSecret = new PinPatternSecret("111222");
        Assert.assertTrue(hashedSecret.equals(pinPatternSecret));
    }

}
