package at.tugraz.tc.cyfile;

import android.app.Application;

import at.tugraz.tc.cyfile.crypto.NoOpCryptoService;
import at.tugraz.tc.cyfile.note.DaggerNoteComponent;
import at.tugraz.tc.cyfile.note.InMemoryNoteRepository;
import at.tugraz.tc.cyfile.note.NoteComponent;
import at.tugraz.tc.cyfile.note.NoteModule;
import at.tugraz.tc.cyfile.note.SecureNoteService;

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
                .noteModule(new NoteModule(new SecureNoteService(
                        new InMemoryNoteRepository(InMemoryNoteRepository.getInitialNotes()), new NoOpCryptoService())))
                .build();
    }

    public NoteComponent getNoteComponent() {
        return mNoteComponent;
    }

}
