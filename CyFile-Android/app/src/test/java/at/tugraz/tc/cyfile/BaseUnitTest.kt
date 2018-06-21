package at.tugraz.tc.cyfile

import org.junit.Before
import org.mockito.MockitoAnnotations

/**
 * Base unit test class holding shared code for all unit tests
 */
abstract class BaseUnitTest {

    @Before
    fun initialization() {
        MockitoAnnotations.initMocks(this)
    }
}
