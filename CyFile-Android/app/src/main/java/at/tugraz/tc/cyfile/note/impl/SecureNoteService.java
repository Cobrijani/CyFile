package at.tugraz.tc.cyfile.note.impl;

import java.util.List;

import at.tugraz.tc.cyfile.crypto.CryptoService;
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
    public List<Note> findAll() {
        List<Note> notes = repository.findAll();
        for (Note note :
                notes) {
            note.setContent(cryptoService.decrypt(note.getContent()));
            note.setTitle(cryptoService.decrypt(note.getTitle()));
        }

        return notes;
    }

    @Override
    public Note findOne(String id) {
        Note note = repository.findOne(id);
        if (note != null) {
            note.setTitle(cryptoService.decrypt(note.getTitle()));
            note.setContent(cryptoService.decrypt(note.getContent()));
            return note;
        } else {
            return null;
        }
    }

    @Override
    public Note save(Note note) {
        if (note == null) {
            throw new IllegalArgumentException("Cannot be null");
        }
        note.setContent(cryptoService.encrypt(note.getContent()));
        note.setTitle(cryptoService.encrypt(note.getTitle()));
        return repository.save(note);
    }

    @Override
    public void delete(String id) {
        repository.delete(id);
    }
}
