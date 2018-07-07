package at.tugraz.tc.cyfile.note

import javax.inject.Singleton

import dagger.Module
import dagger.Provides

/**
 * Dagger module for note feature
 * Created by cobri on 3/21/2018.
 */
@Module
class NoteModule(private val mNoteService: NoteService) {


    @Provides
    @Singleton
    fun providesNoteService(): NoteService {
        return mNoteService
    }
}
