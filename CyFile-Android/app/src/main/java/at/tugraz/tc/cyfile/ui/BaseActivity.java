package at.tugraz.tc.cyfile.ui;

import android.support.v7.app.AppCompatActivity;

import at.tugraz.tc.cyfile.CyFileApplication;
import at.tugraz.tc.cyfile.injection.ActivityComponent;
import at.tugraz.tc.cyfile.injection.DaggerActivityComponent;

public abstract class BaseActivity extends AppCompatActivity {

    private ActivityComponent mActivityComponent;


    public ActivityComponent getActivityComponent() {
        if (mActivityComponent == null) {
            mActivityComponent = DaggerActivityComponent.builder()
                    .applicationComponent(CyFileApplication.get(this).getComponent())
                    .build();
        }
        return mActivityComponent;
    }
}
