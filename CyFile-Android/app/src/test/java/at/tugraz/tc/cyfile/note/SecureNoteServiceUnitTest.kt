package at.tugraz.tc.cyfile.note

import at.tugraz.tc.cyfile.BaseUnitTest
import at.tugraz.tc.cyfile.crypto.CryptoService
import at.tugraz.tc.cyfile.crypto.exceptions.InvalidCryptoOperationException
import at.tugraz.tc.cyfile.domain.Note
import at.tugraz.tc.cyfile.note.impl.SecureNoteService
import junit.framework.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.*
import java.util.*

/**
 * Unit tests for [SecureNoteService]
 * Created by cobri on 3/21/2018.
 */

class SecureNoteServiceUnitTest : BaseUnitTest() {

    private var cryptoService: CryptoService? = null

    private var noteRepository: NoteRepository? = null

    private var secureNoteService: SecureNoteService? = null

    @Before
    fun setup() {
        cryptoService = mock(CryptoService::class.java)
        noteRepository = mock(NoteRepository::class.java)

        // the unit of testing is Simple note service and because of that we mock everything else
        secureNoteService = SecureNoteService(noteRepository!!, cryptoService!!)
    }

    @Test
    @Throws(InvalidCryptoOperationException::class)
    fun findAllShouldDecryptAllNotesAndReturnTheWholeCollection() {
        `when`(noteRepository!!.findAll())
                .thenReturn(Arrays.asList(Note("1", "name1", "content1"), Note("2", "name2", "content2")))

        val notes = secureNoteService!!.findAll()

        verify<CryptoService>(cryptoService, atLeastOnce()).decrypt(ArgumentMatchers.anyString())
        verify<NoteRepository>(noteRepository, times(1)).findAll()
        Assert.assertEquals(2, notes.size)
    }

    @Test
    @Throws(InvalidCryptoOperationException::class)
    fun findAllShouldReturnEmptyCollectionWhenInternalErrorOccurs() {
        `when`(noteRepository!!.findAll())
                .thenReturn(Arrays.asList(Note("1", "name1", "content1"), Note("2", "name2", "content2")))
        `when`<String>(cryptoService!!.decrypt(ArgumentMatchers.anyString()))
                .thenThrow(InvalidCryptoOperationException::class.java)
        val notes = secureNoteService!!.findAll()

        verify<CryptoService>(cryptoService, atLeastOnce()).decrypt(ArgumentMatchers.anyString())
        verify<NoteRepository>(noteRepository, times(1)).findAll()
        Assert.assertEquals(0, notes.size)
    }

    @Test
    @Throws(InvalidCryptoOperationException::class)
    fun findOneShouldFirstDecryptTheNoteThanReturnIt() {
        val n = Note("existing-id", "name", "content")
        `when`<Note>(noteRepository!!.findOne(n.id))
                .thenReturn(n)

        `when`(cryptoService!!.decrypt(n.title!!))
                .thenReturn(n.title)
        `when`(cryptoService!!.decrypt(n.content!!))
                .thenReturn(n.content)

        val note = secureNoteService!!.findOne("existing-id")

        verify<CryptoService>(cryptoService, atLeastOnce()).decrypt(ArgumentMatchers.anyString())
        verify<NoteRepository>(noteRepository, times(1)).findOne("existing-id")
        Assert.assertEquals(n.id, note!!.id)
        Assert.assertEquals(n.title, note.title)
        Assert.assertEquals(n.content, note.content)

    }

    @Test
    @Throws(InvalidCryptoOperationException::class)
    fun findOneShouldNotDecryptAndShouldReturnNullIfNoteDoesNotExist() {
        `when`<Note>(noteRepository!!.findOne("non-existing-id"))
                .thenReturn(null)

        val note = secureNoteService!!.findOne("non-existing-id")

        verify<CryptoService>(cryptoService, times(0)).decrypt(ArgumentMatchers.anyString())
        verify<NoteRepository>(noteRepository, times(1)).findOne("non-existing-id")
        Assert.assertNull(note)
    }

    @Test
    @Throws(InvalidCryptoOperationException::class)
    fun findOneShouldReturnNullIfInternalErrorOccurs() {
        val n = Note("existing-id", "name", "content")
        `when`<Note>(noteRepository!!.findOne(n.id))
                .thenReturn(n)

        `when`(cryptoService!!.decrypt(n.title!!))
                .thenThrow(InvalidCryptoOperationException::class.java)
        `when`(cryptoService!!.decrypt(n.content!!))
                .thenThrow(InvalidCryptoOperationException::class.java)

        val note = secureNoteService!!.findOne("existing-id")

        verify<CryptoService>(cryptoService, atLeastOnce()).decrypt(ArgumentMatchers.anyString())
        verify<NoteRepository>(noteRepository, times(1)).findOne("existing-id")
        Assert.assertNull(note)
    }

    @Test
    @Throws(InvalidCryptoOperationException::class)
    fun saveShouldEncryptNewDataAndUpdateOrInsertToStorage() {
        secureNoteService!!.save(Note("id", "name", "test"))

        verify<CryptoService>(cryptoService, atLeastOnce()).encrypt(ArgumentMatchers.anyString())
        verify<NoteRepository>(noteRepository, times(1)).save(any(Note::class.java))
    }

    @Test(expected = IllegalArgumentException::class)
    fun saveShouldThrowAnErrorIfNoteIsNull() {
        secureNoteService!!.save(null!!)
    }

    @Test
    @Throws(InvalidCryptoOperationException::class)
    fun saveShouldReturnNullIfInternalErrorOccurs() {
        `when`<String>(cryptoService!!.decrypt(ArgumentMatchers.anyString()))
                .thenThrow(InvalidCryptoOperationException::class.java)

        `when`<String>(cryptoService!!.encrypt(ArgumentMatchers.anyString()))
                .thenThrow(InvalidCryptoOperationException::class.java)

        val n = secureNoteService!!.save(Note("id", "name", "test"))

        verify<CryptoService>(cryptoService, atLeastOnce()).encrypt(ArgumentMatchers.anyString())
        Assert.assertNull(n)
    }

    @Test
    fun deleteShouldCallDeletionOfEntityInTheRepository() {
        secureNoteService!!.delete("id")
        verify<NoteRepository>(noteRepository, times(1)).delete("id")
    }


}
