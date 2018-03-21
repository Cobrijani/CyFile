package at.tugraz.tc.cyfile.note;

import java.util.Collections;
import java.util.List;

import at.tugraz.tc.cyfile.domain.Note;

/**
 * Dummy implementation of {@link NoteRepository}
 * Created by cobri on 3/21/2018.
 */

public class InMemoryNoteRepository implements NoteRepository {
    @Override
    public List<Note> findAll() {
        return Collections.emptyList();
    }

    @Override
    public Note findOne(String id) {
        return new Note(id, null, null);
    }

    @Override
    public Note save(Note note) {
        return new Note("id", note.getTitle(), note.getContent());
    }

    @Override
    public void delete(String id) {

    }
}
