package at.tugraz.tc.cyfile.note;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import at.tugraz.tc.cyfile.domain.Note;

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
        //insert
        if (note.getId() == null) {
            note.setId(UUID.randomUUID().toString());
            noteSet.add(note);
            return note;
        } else {
            for (Note n : noteSet) {
                if (n.getId().equals(note.getId())) {
                    n.setTitle(note.getTitle());
                    n.setContent(note.getContent());
                    return n;
                }
            }
        }
        return null;
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

    public static Set<Note> getInitialNotes() {
        Set<Note> initialNotes = new HashSet<>();
        initialNotes.add(new Note("dd95ab42-2e90-11e8-b467-0ed5f89f718b",
                "Secret Note",
                "Lorem Ipsum Dolor sit amet"));
        initialNotes.add(new Note("f123a9a2-2e90-11e8-b467-0ed5f89f718b",
                "A very long note",
                "Nam vehicula tellus euismod, faucibus enim vitae, feugiat risus. Morbi in\n" +
                        "                pulvinar dolor, vitae ultricies diam. Cras sed turpis nec elit laoreet ultricies non\n" +
                        "                a velit. Cras vel nunc lacinia, malesuada felis ac, sodales mi. Aliquam erat\n" +
                        "                volutpat. Donec varius dui sed diam hendrerit, sed scelerisque libero scelerisque.\n" +
                        "                Maecenas pellentesque dapibus massa, a posuere magna. Nam sit amet aliquet dui.\n" +
                        "                Quisque eget eros finibus, pharetra quam ut, rhoncus dui. Sed non urna et nisl\n" +
                        "                semper rhoncus quis sed mauris. Mauris vitae posuere nibh. Cras rutrum diam quis\n" +
                        "                diam sagittis placerat. Vivamus et auctor eros. Sed blandit ornare varius.\n" +
                        "                Suspendisse consectetur commodo turpis sit amet ornare. Quisque consequat, est sed\n" +
                        "                pharetra semper, metus sapien rutrum massa, ac suscipit ligula tortor id tortor.\n" +
                        "\n" +
                        "                Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis\n" +
                        "                egestas. Nam vitae diam eu eros egestas consectetur ac vel nulla. Donec pellentesque\n" +
                        "                nulla non molestie porta. Quisque dignissim dui felis, id congue erat pharetra eu.\n" +
                        "                Etiam et justo maximus, luctus nisi sed, accumsan lorem. Sed laoreet lacinia ex et\n" +
                        "                dignissim. Etiam ac diam ac leo interdum euismod at at tellus. Aenean vitae nisi ut\n" +
                        "                dolor luctus molestie molestie sed quam. Morbi a vestibulum massa, eu malesuada\n" +
                        "                justo. In feugiat aliquam convallis. Vivamus tincidunt erat at vestibulum accumsan.\n" +
                        "                Suspendisse sed mattis ex. Interdum et malesuada fames ac ante ipsum primis in\n" +
                        "                faucibus. Morbi ut metus eget sapien varius cursus quis eu ante. Sed sollicitudin\n" +
                        "                odio non ligula lacinia, ut dapibus est hendrerit. Ut diam metus, mollis ac\n" +
                        "                tristique a, volutpat et quam.\n" +
                        "\n" +
                        "                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque porta sodales\n" +
                        "                orci id aliquet. Cras turpis massa, tincidunt vel magna dignissim, luctus pulvinar\n" +
                        "                elit. In nisi arcu, finibus non pulvinar at, molestie vitae nisl. Aenean id erat non\n" +
                        "                arcu hendrerit tristique vestibulum sit amet tellus. Aliquam erat volutpat. Donec\n" +
                        "                viverra, ex sed egestas egestas, ante lacus blandit nisi, in sagittis ante dui vel\n" +
                        "                ipsum. Curabitur congue, est eu rutrum porta, nulla risus malesuada purus, id\n" +
                        "                molestie turpis sem ac risus. Etiam tempus orci eu arcu volutpat, ut auctor magna\n" +
                        "                finibus. Nunc risus sem, tincidunt at massa tincidunt, placerat finibus nunc.\n" +
                        "                Aliquam erat volutpat. Suspendisse potenti. Sed sed euismod enim. Morbi dignissim\n" +
                        "                tempor nibh sed vehicula. Etiam condimentum neque a tortor dapibus, et vehicula diam\n" +
                        "                feugiat. Curabitur feugiat augue nec porta vehicula."
        ));
        initialNotes.add(new Note("f329aef4-2e90-11e8-b467-0ed5f89f718b", "Empty note", ""));
        return initialNotes;
    }
}
