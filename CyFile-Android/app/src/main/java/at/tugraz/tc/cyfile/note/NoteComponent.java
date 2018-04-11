package at.tugraz.tc.cyfile.note;

import javax.inject.Singleton;

import at.tugraz.tc.cyfile.DisplayNoteActivity;
import at.tugraz.tc.cyfile.MainActivity;
import at.tugraz.tc.cyfile.crypto.CryptoModule;
import dagger.Component;

/**
 * Note component for instantiating note functionality
 * Created by cobri on 3/21/2018.
 */
@Singleton
@Component(modules = {CryptoModule.class, NoteModule.class})
public interface NoteComponent {

    /**
     * Inject into activity
     *
     * @param activity main activity
     */
    void inject(MainActivity activity);

    /**
     * Inject into activity
     * @param activity
     */
    void inject(DisplayNoteActivity activity);
}
