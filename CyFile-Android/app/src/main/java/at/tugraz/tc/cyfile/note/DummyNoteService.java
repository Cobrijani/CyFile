package at.tugraz.tc.cyfile.note;

import java.util.List;

import at.tugraz.tc.cyfile.crypto.CryptoService;
import at.tugraz.tc.cyfile.domain.Note;

/**
 * Dummy implementation of {@link NoteService}
 * Created by cobri on 3/21/2018.
 */

public class DummyNoteService implements NoteService {

    private final NoteRepository repository;

    private final CryptoService cryptoService;

    public DummyNoteService(NoteRepository repository, CryptoService cryptoService) {
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
        note.setContent(cryptoService.encrypt(note.getContent()));
        note.setTitle(cryptoService.encrypt(note.getTitle()));
        return repository.save(note);
    }

    @Override
    public void delete(String id) {
        repository.delete(id);
    }
}
