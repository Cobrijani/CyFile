package at.tugraz.tc.cyfile.note;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import at.tugraz.tc.cyfile.crypto.CryptoService;
import at.tugraz.tc.cyfile.crypto.exceptions.InvalidCryptoOperationException;
import at.tugraz.tc.cyfile.domain.Note;
import at.tugraz.tc.cyfile.note.impl.SecureNoteService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link SecureNoteService}
 * Created by cobri on 3/21/2018.
 */

public class SecureNoteServiceUnitTest {

    private CryptoService cryptoService;

    private NoteRepository noteRepository;

    private SecureNoteService secureNoteService;

    @Before
    public void setup() {
        cryptoService = mock(CryptoService.class);
        noteRepository = mock(NoteRepository.class);

        // the unit of testing is Simple note service and because of that we mock everything else
        secureNoteService = new SecureNoteService(noteRepository, cryptoService);
    }

    @Test
    public void findAllShouldDecryptAllNotesAndReturnTheWholeCollection() throws InvalidCryptoOperationException {
        when(noteRepository.findAll())
                .thenReturn(Arrays.asList(new Note("1", "name1", "content1")
                        , new Note("2", "name2", "content2")));

        List<Note> notes = secureNoteService.findAll();

        verify(cryptoService, atLeastOnce()).decrypt(anyString());
        verify(noteRepository, times(1)).findAll();
        Assert.assertEquals(2, notes.size());
    }

    @Test
    public void findAllShouldReturnEmptyCollectionWhenInternalErrorOccurs() throws InvalidCryptoOperationException {
        when(noteRepository.findAll())
                .thenReturn(Arrays.asList(new Note("1", "name1", "content1")
                        , new Note("2", "name2", "content2")));
        when(cryptoService.decrypt(anyString()))
                .thenThrow(InvalidCryptoOperationException.class);
        List<Note> notes = secureNoteService.findAll();

        verify(cryptoService, atLeastOnce()).decrypt(anyString());
        verify(noteRepository, times(1)).findAll();
        Assert.assertEquals(0, notes.size());
    }

    @Test
    public void findOneShouldFirstDecryptTheNoteThanReturnIt() throws InvalidCryptoOperationException {
        Note n = new Note("existing-id", "name", "content");
        when(noteRepository.findOne(n.getId()))
                .thenReturn(n);

        when(cryptoService.decrypt(n.getTitle()))
                .thenReturn(n.getTitle());
        when(cryptoService.decrypt(n.getContent()))
                .thenReturn(n.getContent());

        Note note = secureNoteService.findOne("existing-id");

        verify(cryptoService, atLeastOnce()).decrypt(anyString());
        verify(noteRepository, times(1)).findOne("existing-id");
        Assert.assertEquals(n.getId(), note.getId());
        Assert.assertEquals(n.getTitle(), note.getTitle());
        Assert.assertEquals(n.getContent(), note.getContent());

    }

    @Test
    public void findOneShouldNotDecryptAndShouldReturnNullIfNoteDoesNotExist() throws InvalidCryptoOperationException {
        when(noteRepository.findOne("non-existing-id"))
                .thenReturn(null);

        Note note = secureNoteService.findOne("non-existing-id");

        verify(cryptoService, times(0)).decrypt(anyString());
        verify(noteRepository, times(1)).findOne("non-existing-id");
        Assert.assertNull(note);
    }

    @Test
    public void findOneShouldReturnNullIfInternalErrorOccurs() throws InvalidCryptoOperationException {
        Note n = new Note("existing-id", "name", "content");
        when(noteRepository.findOne(n.getId()))
                .thenReturn(n);

        when(cryptoService.decrypt(n.getTitle()))
                .thenThrow(InvalidCryptoOperationException.class);
        when(cryptoService.decrypt(n.getContent()))
                .thenThrow(InvalidCryptoOperationException.class);

        Note note = secureNoteService.findOne("existing-id");

        verify(cryptoService, atLeastOnce()).decrypt(anyString());
        verify(noteRepository, times(1)).findOne("existing-id");
        Assert.assertNull(note);
    }

    @Test
    public void saveShouldEncryptNewDataAndUpdateOrInsertToStorage() throws InvalidCryptoOperationException {
        secureNoteService.save(new Note("id", "name", "test"));

        verify(cryptoService, atLeastOnce()).encrypt(anyString());
        verify(noteRepository, times(1)).save(any(Note.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveShouldThrowAnErrorIfNoteIsNull() {
        secureNoteService.save(null);
    }

    @Test
    public void saveShouldReturnNullIfInternalErrorOccurs() throws InvalidCryptoOperationException {
        when(cryptoService.decrypt(anyString()))
                .thenThrow(InvalidCryptoOperationException.class);

        when(cryptoService.encrypt(anyString()))
                .thenThrow(InvalidCryptoOperationException.class);

        Note n = secureNoteService.save(new Note("id", "name", "test"));

        verify(cryptoService, atLeastOnce()).encrypt(anyString());
        Assert.assertNull(n);
    }

    @Test
    public void deleteShouldCallDeletionOfEntityInTheRepository() {
        secureNoteService.delete("id");
        verify(noteRepository, times(1)).delete("id");
    }


}
