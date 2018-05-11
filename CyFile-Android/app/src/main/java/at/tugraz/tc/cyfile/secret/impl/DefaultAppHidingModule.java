package at.tugraz.tc.cyfile.secret.impl;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;

import at.tugraz.tc.cyfile.MainActivity;
import at.tugraz.tc.cyfile.secret.AppHidingModule;

public class DefaultAppHidingModule implements AppHidingModule {

    public static final Class MAIN_ACTIVITY = MainActivity.class;

    @Override
    public void hideApp(Context context) {
        PackageManager p = context.getPackageManager();
        ComponentName componentName = new ComponentName(context, MAIN_ACTIVITY);
        p.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    public void displayApp(Context context) {
        PackageManager p = context.getPackageManager();
        ComponentName componentName = new ComponentName(context, MAIN_ACTIVITY);
        p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }
}
