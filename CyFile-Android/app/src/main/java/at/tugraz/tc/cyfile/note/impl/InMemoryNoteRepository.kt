package at.tugraz.tc.cyfile.note.impl

import at.tugraz.tc.cyfile.domain.Note
import at.tugraz.tc.cyfile.note.NoteRepository
import java.util.*

/**
 * In memory implementation of [NoteRepository]
 * Created by cobri on 3/21/2018.
 */

class InMemoryNoteRepository(private val noteSet: MutableSet<Note>) : NoteRepository {
    override fun findAll(): List<Note> {
        return ArrayList(noteSet)
    }

    override fun findOne(id: String): Note? {
        return noteSet.first { x -> x.id == id }
    }

    override fun save(note: Note): Note {
        for (n in noteSet) {
            if (n.id == note.id) {
                n.title = note.title
                n.content = note.content
                n.dateTimeCreated = note.dateTimeCreated
                n.dateTimeModified = note.dateTimeModified
                return n
            }
        }


        //set id for null
        note.id = UUID.randomUUID().toString()

        noteSet.add(note)
        return note

    }

    override fun delete(id: String) {
        noteSet.filter { x -> x.id == id }
                .forEach { x -> noteSet.remove(x) }

    }

    override fun initialize() {
        //nothing to do
    }
}
