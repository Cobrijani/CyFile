package at.tugraz.tc.cyfile.note.impl;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import at.tugraz.tc.cyfile.domain.Note;
import at.tugraz.tc.cyfile.note.NoteRepository;

public class FileNoteRepository implements NoteRepository {
    private static final String DEFAULT_FILE_NAME = "notes.bin";

    private final String fileName;
    private InMemoryNoteRepository inMemoryNoteRepository;
    private final Context context;
    private boolean initialized = false;

    public FileNoteRepository(Context context, String fileName) {
        if (fileName == null) {
            fileName = DEFAULT_FILE_NAME;
        }
        this.fileName = fileName;
        this.context = context;
    }

    @Override
    public void initialize() {
        Set<Note> notes = loadNotesFromFile();
        inMemoryNoteRepository = new InMemoryNoteRepository(notes);
        initialized = true;
    }

    public InputStream getInputStream() throws FileNotFoundException {
        return context.openFileInput(fileName);
    }

    private Set<Note> loadNotesFromFile() {
        List<Note> notes = new LinkedList<>();
        try (InputStream fis = getInputStream();
             ObjectInputStream is = new ObjectInputStream(fis)) {
            notes = (List<Note>) is.readObject();
            Log.d("File IO", "loaded " + notes.size() + " notes from file");
        } catch (FileNotFoundException e) {
            Log.d("File IO", "file not found, is this the first use?");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new HashSet<>(notes);
    }

    private void saveNotesToFile(List<Note> notes) {
        try (FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
             ObjectOutputStream os = new ObjectOutputStream(fos)) {
            os.writeObject(notes);
            Log.d("File IO", "saved " + notes.size() + " notes to file");
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("This should never happen. " +
                    "Maybe you actually managed to run out of storage?");
        }
    }

    @Override
    public List<Note> findAll() {
        if (!initialized) {
            throw new IllegalStateException("FileNoteRepository was not initialized");
        }
        return inMemoryNoteRepository.findAll();
    }

    @Override
    public Note findOne(String id) {
        if (!initialized) {
            throw new IllegalStateException("FileNoteRepository was not initialized");
        }
        return inMemoryNoteRepository.findOne(id);
    }

    @Override
    public Note save(Note note) {
        if (!initialized) {
            throw new IllegalStateException("FileNoteRepository was not initialized");
        }
        inMemoryNoteRepository.save(note);
        List<Note> notes = inMemoryNoteRepository.findAll();
        if (note.getId().length() == 0) {
            throw new IllegalStateException("Note didn't get an ID assigned by InMemoryNoteRepo");
        }
        saveNotesToFile(notes);
        return note;
    }

    @Override
    public void delete(String id) {
        if (!initialized) {
            throw new IllegalStateException("FileNoteRepository was not initialized");
        }
        inMemoryNoteRepository.delete(id);
        List<Note> notes = inMemoryNoteRepository.findAll();
        if (notes.contains(new Note(id, "", ""))) {
            throw new IllegalStateException("Note didn't get deleted from InMemoryNoteRepo");
        }
        saveNotesToFile(notes);
    }
}
