package at.tugraz.tc.cyfile.note


import at.tugraz.tc.cyfile.BaseUnitTest
import at.tugraz.tc.cyfile.domain.Note
import at.tugraz.tc.cyfile.file.FileHandler
import at.tugraz.tc.cyfile.logging.CyFileLogger
import at.tugraz.tc.cyfile.logging.impl.NoOpLogger
import at.tugraz.tc.cyfile.note.impl.FileBasedNoteRepository
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.*

/**
 * Unit tests for [at.tugraz.tc.cyfile.note.impl.FileBasedNoteRepository]
 * Created by mmalte on 25.04.18.
 */

class FileBasedRepositoryUnitTest : BaseUnitTest() {

    private var logger: CyFileLogger = NoOpLogger()

    private lateinit var sut: FileBasedNoteRepository

    @RelaxedMockK
    private lateinit var fileHandler: FileHandler

    private val notes: MutableList<Note> = ArrayList()

    @Before
    fun setup() {
        every { fileHandler.getInputStream() } returns createInputStream(notes)
        every { fileHandler.getOutputStream() } returns createOutputStream()

        sut = FileBasedNoteRepository(logger, fileHandler)
    }

    @After
    fun tearDown() {
        notes.clear()
    }


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
        notes.add(n1)
        notes.add(n)

        every { fileHandler.getInputStream() } returns createInputStream(notes)


        sut = FileBasedNoteRepository(logger, fileHandler)
        val actual = sut.findAll()
        Assert.assertTrue(actual.size == 2)

        val actual2 = sut.findOne("my-id2")
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
        notes.add(n)

        val nSave = Note("", "name2", "content2")
        every { fileHandler.getInputStream() } returns createInputStream(notes)
        sut = FileBasedNoteRepository(logger, fileHandler)
        val saved = sut.save(nSave)

        Assert.assertNotNull(saved)

        val buffer = (fileHandler.getOutputStream() as ByteArrayOutputStream).toByteArray()
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
        notes.add(n)

        sut = FileBasedNoteRepository(logger, fileHandler)
        sut.delete("my-id1")

        val buffer = (fileHandler.getOutputStream() as ByteArrayOutputStream).toByteArray()
        val `is` = ByteArrayInputStream(buffer)
        val ois = ObjectInputStream(`is`)
        val actual = ois.readObject() as List<Note>

        Assert.assertTrue(actual.isEmpty())
    }
}


