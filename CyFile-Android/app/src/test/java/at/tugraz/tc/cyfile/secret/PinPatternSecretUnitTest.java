package at.tugraz.tc.cyfile.secret;

import com.andrognito.patternlockview.PatternLockView;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class PinPatternSecretUnitTest extends BaseUnitTest {

    @Before
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        Field field = PatternLockView.class.getDeclaredField("sDotCount");
        field.setAccessible(true);
        field.set(null, 3);
    }

    @Test
    public void bothConstructorsShouldGiveEqualValue() {

        Secret secret = new PinPatternSecret("111222");

        Secret secret1 = new PinPatternSecret(Arrays.asList(
                PatternLockView.Dot.of(1, 1),
                PatternLockView.Dot.of(1, 2),
                PatternLockView.Dot.of(2, 2)));

        Assert.assertEquals(secret.getSecretValue(), secret1.getSecretValue());
    }

    @Test
    public void testEqualVerifier() {
        EqualsVerifier.forClass(PinPatternSecret.class)
                .suppress(Warning.ALL_FIELDS_SHOULD_BE_USED)
                .suppress(Warning.NONFINAL_FIELDS)
                .withPrefabValues(PinPatternSecret.class, new PinPatternSecret("111222"), new PinPatternSecret("001122"))
                .withPrefabValues(PatternLockView.Dot.class, PatternLockView.Dot.of(0, 0), PatternLockView.Dot.of(0, 1))
                .usingGetClass()
                .verify();
    }
}
