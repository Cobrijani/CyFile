package at.tugraz.tc.cyfile.logging.impl;

import at.tugraz.tc.cyfile.logging.CyFileLogger;

public class NoOpLogger implements CyFileLogger {
    @Override
    public void d(String tag, String msg) {
    }

    @Override
    public void e(String tag, String msg) {
    }

    @Override
    public void i(String tag, String msg) {

    }

    @Override
    public void w(String tag, String msg) {

    }
}
