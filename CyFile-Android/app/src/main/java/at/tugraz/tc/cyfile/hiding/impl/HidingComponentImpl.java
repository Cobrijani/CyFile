package at.tugraz.tc.cyfile.hiding.impl;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;

import at.tugraz.tc.cyfile.MainActivity;
import at.tugraz.tc.cyfile.hiding.HidingComponent;

public class HidingComponentImpl implements HidingComponent {

    @Override
    public void hideApp(Context context) {
        PackageManager p = context.getPackageManager();
        ComponentName componentName = new ComponentName(context, MainActivity.class);
        p.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    public void showApp(Context context) {
        PackageManager p = context.getPackageManager();
        ComponentName componentName = new ComponentName(context, MainActivity.class);
        p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }
}
