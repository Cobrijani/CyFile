package at.tugraz.tc.cyfile.note.impl

import android.content.Context
import at.tugraz.tc.cyfile.domain.Note
import at.tugraz.tc.cyfile.logging.CyFileLogger
import at.tugraz.tc.cyfile.note.NoteRepository
import java.io.*
import java.util.HashSet
import kotlin.collections.ArrayList


class FileBasedNoteRepository(private val context: Context,
                              private val fileName: String = DEFAULT_FILE_NAME,
                              private val logger: CyFileLogger,
                              private val inputStream: InputStream = context.openFileInput(fileName),
                              private val outputStream: OutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)) : NoteRepository {

    private var inMemoryNoteRepository: InMemoryNoteRepository

    init {
        val notes = loadNotesFromFile()
        inMemoryNoteRepository = InMemoryNoteRepository(notes)
    }

    private fun loadNotesFromFile(): MutableSet<Note> {
        var notes: List<Note> = ArrayList()
        try {
            inputStream.use { fis ->
                ObjectInputStream(fis).use { `is` ->
                    notes = `is`.readObject() as List<Note>
                    logger.d("File IO", "loaded " + notes.size + " notes from file")
                }
            }
        } catch (e: FileNotFoundException) {
            logger.d("File IO", "file not found, is this the first use?")
        } catch (e: IOException) {
            throw IllegalStateException(e)
        } catch (e: ClassNotFoundException) {
            throw IllegalStateException(e)
        } catch (e: ClassCastException) {
            throw IllegalStateException(e)
        }

        return HashSet(notes)
    }

    private fun saveNotesToFile(notes: List<Note>) {
        try {
            outputStream.use { fos ->
                ObjectOutputStream(fos).use { os ->
                    os.writeObject(notes)
                    logger.d("File IO", "saved " + notes.size + " notes to file")
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            throw IllegalStateException("This should never happen. " + "Maybe you actually managed to run out of storage?")
        }

    }

    override fun findAll(): List<Note> {
        return inMemoryNoteRepository.findAll()
    }

    override fun findOne(id: String): Note? {
        return inMemoryNoteRepository.findOne(id)
    }

    override fun save(note: Note): Note {
        inMemoryNoteRepository.save(note)
        val notes = inMemoryNoteRepository.findAll()
        if (note.id.isEmpty()) {
            throw IllegalStateException("Note didn't get an ID assigned by InMemoryNoteRepo")
        }
        saveNotesToFile(notes)
        return note
    }

    override fun delete(id: String) {
        inMemoryNoteRepository.delete(id)
        val notes = inMemoryNoteRepository.findAll()
        saveNotesToFile(notes)
    }

    companion object {
        private const val DEFAULT_FILE_NAME = "notes.bin"
    }

}
