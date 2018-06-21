package at.tugraz.tc.cyfile.note

import at.tugraz.tc.cyfile.domain.Note

/**
 * Class responsible for managing persistence logic for [at.tugraz.tc.cyfile.domain.Note]
 * Created by cobri on 3/21/2018.
 */

interface NoteRepository {

    /**
     * get all notes
     *
     * @return collection of notes
     */
    fun findAll(): List<Note>

    /**
     * Find note by a id
     *
     * @param id id of a note
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
     * Delete a note
     *
     * @param id id of a note
     */
    fun delete(id: String)

    fun initialize()
}
