package at.tugraz.tc.cyfile.secret

import at.tugraz.tc.cyfile.BaseUnitTest
import at.tugraz.tc.cyfile.secret.impl.PinPatternSecret
import com.andrognito.patternlockview.PatternLockView
import org.junit.Assert
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.junit.Before
import org.junit.Test
import java.util.*

class PinPatternSecretUnitTest : BaseUnitTest() {

    @Before
    @Throws(NoSuchFieldException::class, IllegalAccessException::class)
    fun setup() {
        val field = PatternLockView::class.java.getDeclaredField("sDotCount")
        field.isAccessible = true
        field.set(null, 3)
    }

    @Test
    fun bothConstructorsShouldGiveEqualValue() {

        val secret = PinPatternSecret("111222")

        val secret1 = PinPatternSecret(Arrays.asList<PatternLockView.Dot>(
                PatternLockView.Dot.of(1, 1),
                PatternLockView.Dot.of(1, 2),
                PatternLockView.Dot.of(2, 2)))

        Assert.assertEquals(secret.secretValue, secret1.secretValue)
    }

    @Test
    fun testEqualVerifier() {
        EqualsVerifier.forClass(PinPatternSecret::class.java)
                .suppress(Warning.ALL_FIELDS_SHOULD_BE_USED)
                .suppress(Warning.NONFINAL_FIELDS)
                .withPrefabValues(PinPatternSecret::class.java, PinPatternSecret("111222"), PinPatternSecret("001122"))
                .withPrefabValues(PatternLockView.Dot::class.java, PatternLockView.Dot.of(0, 0), PatternLockView.Dot.of(0, 1))
                .usingGetClass()
                .verify()
    }
}
