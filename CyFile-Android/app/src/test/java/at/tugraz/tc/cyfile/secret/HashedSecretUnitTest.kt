package at.tugraz.tc.cyfile.secret

import at.tugraz.tc.cyfile.BaseUnitTest
import at.tugraz.tc.cyfile.crypto.impl.ApacheCodecBase64
import at.tugraz.tc.cyfile.secret.impl.HashedSecret
import at.tugraz.tc.cyfile.secret.impl.PinPatternSecret
import org.junit.Assert
import org.junit.Ignore
import org.junit.Test

class HashedSecretUnitTest : BaseUnitTest() {

    @Test
    @Ignore
    fun bothConstructorsShouldGiveEqualValue() {
        val hashedSecret = HashedSecret(PinPatternSecret("111222"), ApacheCodecBase64())
        val pinPatternSecret = PinPatternSecret("111222")
        Assert.assertTrue(hashedSecret.secretValue == pinPatternSecret.secretValue)
    }

}
