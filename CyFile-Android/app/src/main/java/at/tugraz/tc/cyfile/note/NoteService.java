package at.tugraz.tc.cyfile.note;

import java.util.List;

import at.tugraz.tc.cyfile.domain.Note;

/**
 * Service responsible for managing {@link at.tugraz.tc.cyfile.domain.Note}
 * Created by cobri on 3/21/2018.
 */

public interface NoteService {

    /**
     * Find all notes
     *
     * @return collection of notes
     */
    List<Note> findAll();

    /**
     * Find note by id
     *
     * @param id id of note
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
     * Delete a note by id
     *
     * @param id id of a note
     */
    void delete(String id);
}
