package at.tugraz.tc.cyfile;

import android.app.Application;

import at.tugraz.tc.cyfile.crypto.CryptoServiceImpl;
import at.tugraz.tc.cyfile.note.DaggerNoteComponent;
import at.tugraz.tc.cyfile.note.InMemoryNoteRepository;
import at.tugraz.tc.cyfile.note.NoteComponent;
import at.tugraz.tc.cyfile.note.NoteModule;
import at.tugraz.tc.cyfile.note.SimpleNoteService;

/**
 * Application extended class
 * Created by cobri on 3/21/2018.
 */

public class CyFileApplication extends Application {

    private NoteComponent mNoteComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mNoteComponent = DaggerNoteComponent
                .builder()
                .noteModule(new NoteModule(new SimpleNoteService(new InMemoryNoteRepository(), new CryptoServiceImpl())))
                .build();

    }

    public NoteComponent getNoteComponent() {
        return mNoteComponent;
    }
}
