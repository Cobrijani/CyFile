package at.tugraz.tc.cyfile.note

import at.tugraz.tc.cyfile.BaseUnitTest
import at.tugraz.tc.cyfile.crypto.CryptoService
import at.tugraz.tc.cyfile.crypto.exceptions.InvalidCryptoOperationException
import at.tugraz.tc.cyfile.domain.Note
import at.tugraz.tc.cyfile.note.exceptions.NoteCannotBeSavedException
import at.tugraz.tc.cyfile.note.impl.SecureNoteService
import io.mockk.Ordering
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.*

/**
 * Unit tests for [SecureNoteService]
 * Created by cobri on 3/21/2018.
 */

class SecureNoteServiceUnitTest : BaseUnitTest() {

    @RelaxedMockK
    private lateinit var cryptoService: CryptoService

    @RelaxedMockK
    private lateinit var noteRepository: NoteRepository

    private lateinit var secureNoteService: SecureNoteService

    @Before
    fun setup() {
        secureNoteService = SecureNoteService(noteRepository, cryptoService)
    }

    @Test
    @Throws(InvalidCryptoOperationException::class)
    fun findAllShouldDecryptAllNotesAndReturnTheWholeCollection() {
        every { noteRepository.findAll() } returns Arrays.asList(Note("1", "name1", "content1"), Note("2", "name2", "content2"))
        val notes = secureNoteService.findAll()


        verify { cryptoService.decrypt(any<String>()) }
        verify { noteRepository.findAll() }

        Assert.assertEquals(2, notes.size)
    }

    @Test
    @Throws(InvalidCryptoOperationException::class)
    fun findAllShouldReturnEmptyCollectionWhenInternalErrorOccurs() {

        every { noteRepository.findAll() } returns Arrays.asList(Note("1", "name1", "content1"), Note("2", "name2", "content2"))
        every { cryptoService.decrypt(any<String>()) } throws InvalidCryptoOperationException()

        val notes = secureNoteService.findAll()

        verify { cryptoService.decrypt(any<String>()) }
        verify { noteRepository.findAll() }

        Assert.assertEquals(0, notes.size)
    }

    @Test
    @Throws(InvalidCryptoOperationException::class)
    fun findOneShouldFirstDecryptTheNoteThanReturnIt() {
        val n = Note("existing-id", "name", "content")

        every { noteRepository.findOne(n.id) } returns n

        every { cryptoService.decrypt(n.title) }.returns(n.title)
        every { cryptoService.decrypt(n.content) }.returns(n.content)

        val note = secureNoteService.findOne("existing-id")

        verify(Ordering.UNORDERED, false, 1, 10) { cryptoService.decrypt(any<String>()) }
        verify(Ordering.UNORDERED, false, 1, 10) { noteRepository.findOne("existing-id") }
        Assert.assertEquals(n.id, note!!.id)
        Assert.assertEquals(n.title, note.title)
        Assert.assertEquals(n.content, note.content)

    }

    @Test
    @Throws(InvalidCryptoOperationException::class)
    fun findOneShouldNotDecryptAndShouldReturnNullIfNoteDoesNotExist() {

        every { noteRepository.findOne("non-existing-id") } returns null

        val note = secureNoteService.findOne("non-existing-id")

        verify(Ordering.UNORDERED, false, exactly = 0) { cryptoService.decrypt(any<String>()) }
        verify(Ordering.UNORDERED, false, 1, 10) { noteRepository.findOne("non-existing-id") }

        Assert.assertNull(note)
    }

    @Test
    @Throws(InvalidCryptoOperationException::class)
    fun findOneShouldReturnNullIfInternalErrorOccurs() {
        val n = Note("existing-id", "name", "content")

        every { noteRepository.findOne(n.id) } returns n

        every { cryptoService.decrypt(n.title) } throws InvalidCryptoOperationException()

        every { cryptoService.decrypt(n.content) } throws InvalidCryptoOperationException()

        val note = secureNoteService.findOne("existing-id")

        verify { cryptoService.decrypt(any<String>()) }
        verify { noteRepository.findOne("existing-id") }

        Assert.assertNull(note)
    }

    @Test
    @Throws(InvalidCryptoOperationException::class)
    fun saveShouldEncryptNewDataAndUpdateOrInsertToStorage() {
        secureNoteService.save(Note("id", "name", "test"))

        verify { cryptoService.encrypt(any<String>()) }
        verify { noteRepository.save(any()) }
    }

    @Test(expected = NoteCannotBeSavedException::class)
    fun saveShouldReturnErrorIFErrorOccurs() {
        every { cryptoService.decrypt(any<String>()) } throws InvalidCryptoOperationException()

        every { cryptoService.encrypt(any<String>()) } throws InvalidCryptoOperationException()

        secureNoteService.save(Note("id", "name", "test"))

    }

    @Test
    fun deleteShouldCallDeletionOfEntityInTheRepository() {
        secureNoteService.delete("id")
        verify { noteRepository.delete("id") }
    }


}