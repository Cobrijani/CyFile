package at.tugraz.tc.cyfile.note

import at.tugraz.tc.cyfile.domain.Note

/**
 * Service responsible for managing [at.tugraz.tc.cyfile.domain.Note]
 * Created by cobri on 3/21/2018.
 */

interface NoteService {

    /**
     * Find all notes
     *
     * @return collection of notes
     */
    fun findAll(): List<Note>

    /**
     * Find note by id
     *
     * @param id id of note
     * @return a note
     */
    fun findOne(id: String): Note?

    /**
     * Save a note
     *
     * @param note note to be saved
     * @return saved note
     */
    fun save(note: Note): Note

    /**
     * Delete a note by id
     *
     * @param id id of a note
     */
    fun delete(id: String)
}
