package at.tugraz.tc.cyfile.note;


import android.content.Context;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import at.tugraz.tc.cyfile.domain.Note;
import at.tugraz.tc.cyfile.logging.CyFileLogger;
import at.tugraz.tc.cyfile.note.impl.FileNoteRepository;

import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Unit tests for {@link at.tugraz.tc.cyfile.note.impl.FileNoteRepository}
 * Created by mmalte on 25.04.18.
 */

@RunWith(MockitoJUnitRunner.class)
public class FileNoteRepositoryUnitTest {

    public FileNoteRepository fileNoteRepository;

    @Mock
    private Context context;

    @Mock
    private CyFileLogger logger;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }


    private InputStream createInputStream(List<Note> notes) throws IOException {
        ByteArrayOutputStream bas = new ByteArrayOutputStream(2048);
        ObjectOutputStream oos = new ObjectOutputStream(bas);
        oos.writeObject(notes);

        byte[] buffer = bas.toByteArray();
        return new ByteArrayInputStream(buffer);
    }

    @Test
    public void loadNotesFromFileWithGeneratedInputStream() throws Exception {
        Note n = new Note("my-id1", "name", "content");
        Note n1 = new Note("my-id2", "name2", "content2");
        List<Note> notes = new LinkedList<>();
        notes.add(n1);
        notes.add(n);

        FileNoteRepository spyRepository = Mockito.spy(new FileNoteRepository(context, null, logger));

        Mockito.doReturn(createInputStream(notes)).when(spyRepository).getInputStream();

        spyRepository.initialize();

        List<Note> actual = spyRepository.findAll();
        Assert.assertTrue(actual.size() == 2);

        Note actual2 = spyRepository.findOne("my-id2");
        Assert.assertNotNull(actual2);
        Assert.assertEquals("name2", actual2.getTitle());
    }

    private OutputStream createOutputStream() throws IOException {
        ByteArrayOutputStream bas = new ByteArrayOutputStream(2048);

        return bas;
    }

    @Test
    public void saveNoteToFileWithSuccess() throws Exception {
        Note n = new Note("my-id1", "name", "content");
        Note n1 = new Note("my-id2", "name2", "content2");
        List<Note> notes = new LinkedList<>();
        notes.add(n1);

        FileNoteRepository spyRepository = Mockito.spy(new FileNoteRepository(context, null, logger));

        ByteArrayOutputStream os = (ByteArrayOutputStream) createOutputStream();

        Mockito.doReturn(os).when(spyRepository).getOutputStream();
        Mockito.doReturn(createInputStream(notes)).when(spyRepository).getInputStream();

        spyRepository.initialize();

        Note actual = spyRepository.save(n);
        Assert.assertNotNull(actual);

        byte[] buffer = os.toByteArray();
        ByteArrayInputStream is = new ByteArrayInputStream(buffer);

        Mockito.doReturn(is).when(spyRepository).getInputStream();

        List<Note> actualList = spyRepository.findAll();
        Assert.assertNotNull(actualList);
        //Assert.assertTrue(actualList.size() == 2);
    }
}


