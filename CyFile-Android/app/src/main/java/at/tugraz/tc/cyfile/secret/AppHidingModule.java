package at.tugraz.tc.cyfile.secret;

import android.content.Context;

public interface AppHidingModule {

    void hideApp(Context context);

    void displayApp(Context context);
}
