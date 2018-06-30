package at.tugraz.tc.cyfile.note


import android.content.Context
import at.tugraz.tc.cyfile.BaseUnitTest
import at.tugraz.tc.cyfile.domain.Note
import at.tugraz.tc.cyfile.logging.CyFileLogger
import at.tugraz.tc.cyfile.logging.impl.NoOpLogger
import at.tugraz.tc.cyfile.note.impl.FileBasedNoteRepository
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert
import org.junit.Test
import java.io.*
import java.util.*

/**
 * Unit tests for [at.tugraz.tc.cyfile.note.impl.FileBasedNoteRepository]
 * Created by mmalte on 25.04.18.
 */

class FileBasedRepositoryUnitTest : BaseUnitTest() {

    @RelaxedMockK
    private lateinit var context: Context

    private var logger: CyFileLogger = NoOpLogger()


    @Throws(IOException::class)
    private fun createInputStream(notes: List<Note>): InputStream {
        val bas = ByteArrayOutputStream(2048)
        val oos = ObjectOutputStream(bas)
        oos.writeObject(notes)

        val buffer = bas.toByteArray()
        return ByteArrayInputStream(buffer)
    }

    @Test
    @Throws(Exception::class)
    fun loadNotesFromFileWithGeneratedInputStream() {
        val n = Note("my-id1", "name", "content")
        val n1 = Note("my-id2", "name2", "content2")
        val notes = LinkedList<Note>()
        notes.add(n1)
        notes.add(n)

        val spyRepository = FileBasedNoteRepository(context, logger = logger,
                inputStream = createInputStream(notes),
                outputStream = createOutputStream())

        val actual = spyRepository.findAll()
        Assert.assertTrue(actual.size == 2)

        val actual2 = spyRepository.findOne("my-id2")
        Assert.assertNotNull(actual2)
        Assert.assertEquals("name2", actual2!!.title)
    }

    @Throws(IOException::class)
    private fun createOutputStream(): OutputStream {
        return ByteArrayOutputStream(2048)
    }

    @Test
    @Throws(Exception::class)
    fun saveNoteToFileWithSuccess() {
        val n = Note("my-id1", "name", "content")
        val notes = LinkedList<Note>()
        notes.add(n)

        val nSave = Note("", "name2", "content2")
        val os = createOutputStream() as ByteArrayOutputStream
        val spyRepository = FileBasedNoteRepository(context, logger = logger,
                inputStream = createInputStream(notes),
                outputStream = os)

        val saved = spyRepository.save(nSave)
        Assert.assertNotNull(saved)

        val buffer = os.toByteArray()
        val `is` = ByteArrayInputStream(buffer)
        val ois = ObjectInputStream(`is`)
        val actual = ois.readObject() as List<Note>

        Assert.assertTrue(actual.size == 2)
        Assert.assertTrue(actual.contains(nSave))
    }

   @Test
    @Throws(Exception::class)
    fun testDeleteWithSuccess() {
        val n = Note("my-id1", "name", "content")
        val notes = LinkedList<Note>()
        notes.add(n)

        val os = createOutputStream() as ByteArrayOutputStream

        val spyRepository = FileBasedNoteRepository(context, logger = logger,
                inputStream = createInputStream(notes),
                outputStream = os)

        spyRepository.delete("my-id1")

        val buffer = os.toByteArray()
        val `is` = ByteArrayInputStream(buffer)
        val ois = ObjectInputStream(`is`)
        val actual = ois.readObject() as List<Note>

        Assert.assertTrue(actual.isEmpty())
    }
}


