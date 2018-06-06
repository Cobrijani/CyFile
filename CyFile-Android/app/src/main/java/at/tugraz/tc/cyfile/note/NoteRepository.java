package at.tugraz.tc.cyfile.note;

import java.util.List;

import at.tugraz.tc.cyfile.domain.Note;

/**
 * Class responsible for managing persistence logic for {@link at.tugraz.tc.cyfile.domain.Note}
 * Created by cobri on 3/21/2018.
 */

public interface NoteRepository {

    void purge();

    /**
     * get all notes
     *
     * @return collection of notes
     */
    List<Note> findAll();

    /**
     * Find note by a id
     *
     * @param id id of a note
     * @return a note
     */
    Note findOne(String id);

    /**
     * Save a note
     *
     * @param note note to be saved
     * @return saved note
     */
    Note save(Note note);

    /**
     * Delete a note
     *
     * @param id id of a note
     */
    void delete(String id);

    void initialize();
}
