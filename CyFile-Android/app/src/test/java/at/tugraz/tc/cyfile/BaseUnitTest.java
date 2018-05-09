package at.tugraz.tc.cyfile;

import org.junit.Before;
import org.mockito.MockitoAnnotations;

/**
 * Base unit test class holding shared code for all unit tests
 */
public abstract class BaseUnitTest {

    @Before
    public void initialization() {
        MockitoAnnotations.initMocks(this);
    }
}
