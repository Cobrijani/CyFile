package at.tugraz.tc.cyfile.hiding;

import android.content.Context;

import at.tugraz.tc.cyfile.logging.CyFileLogger;

public interface HidingComponent {

    void hideApp(Context context, CyFileLogger logger);

    void showApp(Context context, CyFileLogger logger);
}
