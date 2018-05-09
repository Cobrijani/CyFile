package at.tugraz.tc.cyfile.note;


import android.content.Context;
import android.test.mock.MockContext;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import at.tugraz.tc.cyfile.domain.Note;
import at.tugraz.tc.cyfile.note.impl.FileNoteRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link at.tugraz.tc.cyfile.note.impl.FileNoteRepository}
 * Created by mmalte on 25.04.18.
 */
//in before:
//create list with notes
//serialize list into FileInputStream and give back when context.openFileInput() is called
//andere Tests nachsehen

public class FileNoteRepositoryUnitTest {

    private FileNoteRepository fileNoteRepository;

    @Mock
    private ObjectInputStream oi;

    @Mock
    private Context context;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
//        fileNoteRepository = new FileNoteRepository(null, context, anyString());
    }

    //scheiß streams
    private InputStream createInputStream(Set<Note> notes) {

        return new ByteArrayInputStream(notes.toString().getBytes());
    }

    @Test
    @Ignore
    public void findAllShouldReturnCorrectNumberOfEntities() throws Exception {
        Note n = new Note(null, "name", "content");
        Note n1 = new Note("my-id2", "name2", "content2");
        Set<Note> notes = new HashSet<>();
        notes.add(n1);
        notes.add(n);

        when(context.openFileInput(anyString())).thenReturn((FileInputStream) createInputStream(notes));

        List<Note> actual = fileNoteRepository.findAll();

        //since it is new entity the repository should assign the id
        Assert.assertFalse(actual.isEmpty());
    }

}
