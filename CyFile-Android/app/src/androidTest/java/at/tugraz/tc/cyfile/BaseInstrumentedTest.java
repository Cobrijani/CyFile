package at.tugraz.tc.cyfile;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;

@RunWith(AndroidJUnit4.class)
public abstract class BaseInstrumentedTest {

    protected CyFileApplication app;


    @Before
    public void initialization() {
        MockitoAnnotations.initMocks(this);

        app = (CyFileApplication)
                InstrumentationRegistry
                        .getInstrumentation()
                        .getTargetContext()
                        .getApplicationContext();
    }
}
