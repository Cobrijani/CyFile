package at.tugraz.tc.cyfile.logging;

import android.util.Log;

public class AndroidLogger implements CyFileLogger {

    @Override
    public void d(String tag, String msg) {
        Log.d(tag, msg);
    }

    @Override
    public void e(String tag, String msg) {
        Log.e(tag, msg);
    }
}