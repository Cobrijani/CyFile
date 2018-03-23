package at.tugraz.tc.cyfile.note;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module for note feature
 * Created by cobri on 3/21/2018.
 */
@Module
public class NoteModule {

    public NoteService mNoteService;

    public NoteModule(NoteService service) {
        this.mNoteService = service;
    }


    @Provides
    @Singleton
    public NoteService providesNoteService() {
        return mNoteService;

    }
}
