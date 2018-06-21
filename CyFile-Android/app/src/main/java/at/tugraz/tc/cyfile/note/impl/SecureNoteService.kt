package at.tugraz.tc.cyfile.note.impl

import at.tugraz.tc.cyfile.crypto.CryptoService
import at.tugraz.tc.cyfile.crypto.exceptions.InvalidCryptoOperationException
import at.tugraz.tc.cyfile.domain.Note
import at.tugraz.tc.cyfile.note.NoteRepository
import at.tugraz.tc.cyfile.note.NoteService
import java.util.ArrayList
import java.util.Date
import kotlin.Comparator
import kotlin.String

/**
 * Secure implementation of [NoteService]
 *
 *
 * Which means everything will be encrypted, except the id,
 * before saving into storage and decrypted when retrieving from it.
 *
 * Created by cobri on 3/21/2018.
 */

class SecureNoteService(private val repository: NoteRepository, private val cryptoService: CryptoService) : NoteService {

    override fun findAll(): List<Note> {
        val notes = repository.findAll()
        val retVal = ArrayList<Note>()
        try {
            for (note in notes) {
                val create = Note(note.id, cryptoService.decrypt(note.title!!), cryptoService.decrypt(note.content!!))
                create.dateTimeCreated = note.dateTimeCreated
                create.dateTimeModified = note.dateTimeModified
                retVal.add(create)
            }
        } catch (e: InvalidCryptoOperationException) {
            e.printStackTrace()
            return emptyList()
        }

        retVal.sortWith(Comparator { n1, n2 -> -1 * n1.dateTimeModified!!.compareTo(n2.dateTimeModified!!) })

        return retVal
    }

    override fun findOne(id: String): Note? {
        val note = repository.findOne(id)
        if (note != null) {
            return try {
                val newNote = Note(note.id, cryptoService.decrypt(note.title!!), cryptoService.decrypt(note.content!!))
                newNote.dateTimeCreated = note.dateTimeCreated
                newNote.dateTimeModified = note.dateTimeModified
                newNote
            } catch (e: InvalidCryptoOperationException) {
                e.printStackTrace()
                null
            }

        }
        return null
    }

    override fun save(note: Note): Note {
        return try {
            note.content = cryptoService.encrypt(note.content!!)
            note.title = cryptoService.encrypt(note.title!!)
            note.dateTimeModified = Date().time

            repository.save(note)
        } catch (e: InvalidCryptoOperationException) {
            e.printStackTrace()
            note
        }

    }

    override fun delete(id: String) {
        repository.delete(id)
    }
}
