package at.tugraz.tc.cyfile.note;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import at.tugraz.tc.cyfile.domain.Note;

/**
 * Unit tests for {@link InMemoryNoteRepository}
 * Created by cobri on 3/21/2018.
 */

public class InMemoryNoteRepositoryUnitTest {

    private Set<Note> notes = new HashSet<>();


    private InMemoryNoteRepository inMemoryNoteRepository;

    @Before
    public void setup() {
        inMemoryNoteRepository = new InMemoryNoteRepository(notes);
    }

    @After
    public void tearDown() {
        notes.clear();
    }

    @Test
    public void findAllShouldReturnCorrectNumberOfEntities() {
        notes.add(new Note("id", null, null));

        Assert.assertEquals(1, inMemoryNoteRepository.findAll().size());
    }

    @Test
    public void findOneShouldReturnEntityIfExists() {
        Note n = new Note("my-id23", "name", "content");
        Note n1 = new Note("my-id2", "name2", "content2");
        notes.add(n);
        notes.add(n1);

        Note note = inMemoryNoteRepository.findOne("my-id23");

        Assert.assertEquals(n.getId(), note.getId());
        Assert.assertEquals(n.getTitle(), note.getTitle());
        Assert.assertEquals(n.getContent(), note.getContent());
    }

    @Test
    public void findOneShouldReturnNullIfEntityDoesNotExists() {
        Note n = new Note("my-id", "name", "content");
        Note n1 = new Note("my-id2", "name2", "content2");
        notes.add(n);
        notes.add(n1);

        Note note = inMemoryNoteRepository.findOne("non-existing-id");

        Assert.assertEquals(null, note);
    }

    @Test
    public void saveShouldInsertNonExistingEntity() {
        Note n = new Note(null, "name", "content");
        Note n1 = new Note("my-id2", "name2", "content2");
        notes.add(n1);

        Note saved = inMemoryNoteRepository.save(n);

        //since it is new entity the repository should assign the id
        Assert.assertNotNull(n.getId());
        Assert.assertEquals(n.getTitle(), saved.getTitle());
        Assert.assertEquals(n.getContent(), saved.getContent());

    }

    @Test
    public void saveShouldUpdateExistingEntity() {
        Note n = new Note("my-id1", "name", "content");
        Note n1 = new Note("my-id2", "name2", "content2");
        notes.add(n1);
        notes.add(n);

        n.setTitle("Changed title");
        Note saved = inMemoryNoteRepository.save(n);

        Assert.assertEquals(saved, n);
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveShouldThrowAnErrorIfEntityIsNotValid() {
        Note n = new Note("my-id1", "name", "content");
        Note n1 = new Note("my-id2", "name2", "content2");
        notes.add(n1);
        notes.add(n);

        inMemoryNoteRepository.save(null);

    }

    @Test
    public void deleteShouldDeleteEntityIfExists() {
        Note n = new Note("my-id1", "name", "content");
        Note n1 = new Note("my-id2", "name2", "content2");
        notes.add(n1);
        notes.add(n);

        inMemoryNoteRepository.delete("my-id1");

        Assert.assertTrue(!notes.contains(n));
        Assert.assertTrue(notes.size() == 1);
    }

    @Test
    public void deleteShouldNotDeleteEntityIfNotExists() {
        Note n = new Note("my-id1", "name", "content");
        Note n1 = new Note("my-id2", "name2", "content2");
        notes.add(n1);
        notes.add(n);

        inMemoryNoteRepository.delete("non-existing-id");

        Assert.assertTrue(notes.contains(n));
        Assert.assertTrue(notes.contains(n1));
        Assert.assertTrue(notes.size() == 2);
    }


}
