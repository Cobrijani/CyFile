package at.tugraz.tc.cyfile

import io.mockk.MockKAnnotations
import org.junit.Before

/**
 * Base unit test class holding shared code for all unit tests
 */
abstract class BaseUnitTest {

    @Before
    fun initialization() = MockKAnnotations.init(this)
}
