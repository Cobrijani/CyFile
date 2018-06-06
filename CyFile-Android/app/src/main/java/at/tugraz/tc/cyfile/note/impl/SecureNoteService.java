package at.tugraz.tc.cyfile.note.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import at.tugraz.tc.cyfile.crypto.CryptoService;
import at.tugraz.tc.cyfile.crypto.exceptions.InvalidCryptoOperationException;
import at.tugraz.tc.cyfile.domain.Note;
import at.tugraz.tc.cyfile.note.NoteRepository;
import at.tugraz.tc.cyfile.note.NoteService;

/**
 * Secure implementation of {@link NoteService}
 * <p>
 * Which means everything will be encrypted, except the id,
 * before saving into storage and decrypted when retrieving from it.
 * </p>
 * Created by cobri on 3/21/2018.
 */

public class SecureNoteService implements NoteService {

    private final NoteRepository repository;

    private final CryptoService cryptoService;

    public SecureNoteService(NoteRepository repository, CryptoService cryptoService) {
        this.repository = repository;
        this.cryptoService = cryptoService;
    }

    @Override
    public void purgeRepository() {
        this.repository.purge();
    }

    @Override
    public List<Note> findAll() {
        List<Note> notes = repository.findAll();
        List<Note> retVal = new ArrayList<>();
        try {
            for (Note note :
                    notes) {
                Note create = new Note();
                create.setContent(cryptoService.decrypt(note.getContent()));
                create.setTitle(cryptoService.decrypt(note.getTitle()));
                create.setId(note.getId());
                create.setDateTimeCreated(note.getDateTimeCreated());
                create.setDateTimeModified(note.getDateTimeModified());
                retVal.add(create);
            }
        } catch (InvalidCryptoOperationException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }

        Collections.sort(retVal, (n1, n2) -> -1 * n1.getDateTimeModified().compareTo(n2.getDateTimeModified()));

        return retVal;
    }

    @Override
    public Note findOne(String id) {
        Note note = repository.findOne(id);
        if (note != null) {
            try {
                Note newNote = new Note();
                newNote.setTitle(cryptoService.decrypt(note.getTitle()));
                newNote.setContent(cryptoService.decrypt(note.getContent()));
                newNote.setId(note.getId());
                newNote.setDateTimeCreated(note.getDateTimeCreated());
                newNote.setDateTimeModified(note.getDateTimeModified());
                return newNote;
            } catch (InvalidCryptoOperationException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    @Override
    public Note save(Note note) {
        if (note == null) {
            throw new IllegalArgumentException("Cannot be null");
        }
        try {
            note.setContent(cryptoService.encrypt(note.getContent()));
            note.setTitle(cryptoService.encrypt(note.getTitle()));
            if (note.getDateTimeCreated() == null) {
                note.setDateTimeCreated(new Date().getTime());
            }

            note.setDateTimeModified(new Date().getTime());

            return repository.save(note);
        } catch (InvalidCryptoOperationException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void delete(String id) {
        repository.delete(id);
    }
}
