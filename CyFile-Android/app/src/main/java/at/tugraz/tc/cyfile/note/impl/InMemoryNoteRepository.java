package at.tugraz.tc.cyfile.note.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import at.tugraz.tc.cyfile.domain.Note;
import at.tugraz.tc.cyfile.note.NoteRepository;

/**
 * In memory implementation of {@link NoteRepository}
 * Created by cobri on 3/21/2018.
 */

public class InMemoryNoteRepository implements NoteRepository {

    private final Set<Note> noteSet;

    public InMemoryNoteRepository() {
        this.noteSet = new HashSet<>();
    }

    public InMemoryNoteRepository(Set<Note> noteSet) {
        this.noteSet = noteSet;
    }

    @Override
    public List<Note> findAll() {
        return new ArrayList<>(noteSet);
    }

    @Override
    public Note findOne(String id) {
        for (Note note :
                noteSet) {
            if (note.getId().equals(id)) {
                return note;
            }
        }
        return null;
    }

    @Override
    public Note save(Note note) {
        if (note == null) {
            throw new IllegalArgumentException("Cannot be null");
        }

        for (Note n : noteSet) {
            if (n.getId().equals(note.getId())) {
                n.setTitle(note.getTitle());
                n.setContent(note.getContent());
                return n;
            }
        }
        //set id for null
        if (note.getId() == null) {
            note.setId(UUID.randomUUID().toString());
        }
        noteSet.add(note);
        return note;

    }

    @Override
    public void delete(String id) {
        for (Note note :
                noteSet) {
            if (note.getId().equals(id)) {
                noteSet.remove(note);
                return;
            }
        }
    }
}
