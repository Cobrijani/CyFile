package at.tugraz.tc.cyfile.logging.impl;

import android.util.Log;

import at.tugraz.tc.cyfile.logging.CyFileLogger;

public class AndroidLogger implements CyFileLogger {

    @Override
    public void d(String tag, String msg) {
        Log.d(tag, msg);
    }

    @Override
    public void e(String tag, String msg) {
        Log.e(tag, msg);
    }

    @Override
    public void i(String tag, String msg) {
        Log.i(tag, msg);
    }

    @Override
    public void w(String tag, String msg) {
        Log.w(tag, msg);
    }
}
