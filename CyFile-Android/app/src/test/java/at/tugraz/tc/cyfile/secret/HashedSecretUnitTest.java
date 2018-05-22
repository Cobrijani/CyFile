package at.tugraz.tc.cyfile.secret;

import android.util.Base64;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;

import at.tugraz.tc.cyfile.BaseUnitTest;
import at.tugraz.tc.cyfile.secret.impl.HashedSecret;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class HashedSecretUnitTest extends BaseUnitTest {


    @Mock
    Base64 mock;

    @Test
    public void bothConstructorsShouldGiveEqualValue() {

        Secret secret = new HashedSecret("111222");

        Secret secret1 = new HashedSecret(() -> "111222");

        Assert.assertEquals(secret.getSecretValue(), secret1.getSecretValue());
    }

    @Test
    public void testEqualVerifier() {
        EqualsVerifier.forClass(HashedSecret.class)
                .suppress(Warning.ALL_FIELDS_SHOULD_BE_USED)
                .suppress(Warning.NONFINAL_FIELDS)
                .usingGetClass()
                .verify();
    }
}
