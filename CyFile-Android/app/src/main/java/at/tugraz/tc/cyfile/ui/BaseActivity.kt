package at.tugraz.tc.cyfile.ui

import android.support.v7.app.AppCompatActivity

import at.tugraz.tc.cyfile.CyFileApplication
import at.tugraz.tc.cyfile.injection.ActivityComponent
import at.tugraz.tc.cyfile.injection.DaggerActivityComponent

abstract class BaseActivity : AppCompatActivity() {

    private lateinit var mActivityComponent: ActivityComponent


    val activityComponent: ActivityComponent
        get() {
            mActivityComponent = DaggerActivityComponent.builder()
                    .applicationComponent(CyFileApplication[this].component)
                    .build()
            return mActivityComponent
        }
}
