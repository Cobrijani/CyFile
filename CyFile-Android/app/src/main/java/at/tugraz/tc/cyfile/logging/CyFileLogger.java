package at.tugraz.tc.cyfile.logging;

public interface CyFileLogger {
    void d(String tag, String msg);

    void e(String tag, String msg);

    void i(String tag, String msg);

    void w(String tag, String msg);
}
