package at.tugraz.tc.cyfile.logging;

public class NoOpLogger implements CyFileLogger {
    @Override
    public void d(String tag, String msg) {}

    @Override
    public void e(String tag, String msg) {}
}
