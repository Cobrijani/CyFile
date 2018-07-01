package at.tugraz.tc.cyfile.note

import at.tugraz.tc.cyfile.BaseUnitTest
import at.tugraz.tc.cyfile.domain.Note
import at.tugraz.tc.cyfile.note.impl.InMemoryNoteRepository
import org.junit.Assert
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.*

/**
 * Unit tests for [InMemoryNoteRepository]
 * Created by cobri on 3/21/2018.
 */

class InMemoryNoteRepositoryUnitTest : BaseUnitTest() {

    private val notes = HashSet<Note>()


    private lateinit var inMemoryNoteRepository: InMemoryNoteRepository

    @Before
    fun setup() {
        inMemoryNoteRepository = InMemoryNoteRepository(notes)
    }

    @After
    fun tearDown() {
        notes.clear()
    }

    @Test
    fun findAllShouldReturnCorrectNumberOfEntities() {
        notes.add(Note("id", "", ""))
        Assert.assertEquals(1, inMemoryNoteRepository.findAll().size)
    }

    @Test
    fun findOneShouldReturnEntityIfExists() {
        val n = Note("my-id23", "name", "content")
        val n1 = Note("my-id2", "name2", "content2")
        notes.add(n)
        notes.add(n1)

        val note = inMemoryNoteRepository.findOne("my-id23")

        Assert.assertEquals(n.id, note!!.id)
        Assert.assertEquals(n.title, note.title)
        Assert.assertEquals(n.content, note.content)
    }

    @Test
    fun findOneShouldReturnNullIfEntityDoesNotExists() {
        val n = Note("my-id", "name", "content")
        val n1 = Note("my-id2", "name2", "content2")
        notes.add(n)
        notes.add(n1)

        val note = inMemoryNoteRepository.findOne("non-existing-id")

        Assert.assertEquals(null, note)
    }

    @Test
    fun saveShouldInsertNonExistingEntity() {
        val n = Note("id1", "name", "content")
        val n1 = Note("my-id2", "name2", "content2")
        notes.add(n1)

        val saved = inMemoryNoteRepository.save(n)

        //since it is new entity the repository should assign the id
        Assert.assertNotNull(n.id)
        Assert.assertEquals(n.title, saved.title)
        Assert.assertEquals(n.content, saved.content)

    }

    @Test
    fun saveShouldUpdateExistingEntity() {
        val n = Note("my-id1", "name", "content")
        val n1 = Note("my-id2", "name2", "content2")
        notes.add(n1)
        notes.add(n)

        n.title = "Changed title"
        val saved = inMemoryNoteRepository.save(n)

        Assert.assertEquals(saved, n)
    }

    @Test
    fun deleteShouldDeleteEntityIfExists() {
        val n = Note("my-id1", "name", "content")
        val n1 = Note("my-id2", "name2", "content2")
        notes.add(n1)
        notes.add(n)

        inMemoryNoteRepository.delete("my-id1")

        Assert.assertTrue(!notes.contains(n))
        Assert.assertTrue(notes.size == 1)
    }

    @Test
    fun deleteShouldNotDeleteEntityIfNotExists() {
        val n = Note("my-id1", "name", "content")
        val n1 = Note("my-id2", "name2", "content2")
        notes.add(n1)
        notes.add(n)

        inMemoryNoteRepository.delete("non-existing-id")

        Assert.assertTrue(notes.contains(n))
        Assert.assertTrue(notes.contains(n1))
        Assert.assertTrue(notes.size == 2)
    }


}
