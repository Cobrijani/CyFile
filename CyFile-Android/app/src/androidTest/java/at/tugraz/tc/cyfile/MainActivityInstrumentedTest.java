package at.tugraz.tc.cyfile;

import android.content.Intent;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

import at.tugraz.tc.cyfile.crypto.DummyKeyVaultService;
import at.tugraz.tc.cyfile.crypto.KeyVaultService;
import at.tugraz.tc.cyfile.async.AsyncModule;
import at.tugraz.tc.cyfile.crypto.KeyVaultService;
import at.tugraz.tc.cyfile.domain.Note;
import at.tugraz.tc.cyfile.injection.ApplicationComponent;
import at.tugraz.tc.cyfile.injection.DaggerApplicationComponent;
import at.tugraz.tc.cyfile.logging.NoOpLogger;
import at.tugraz.tc.cyfile.logging.NoOpLogger;
import at.tugraz.tc.cyfile.note.NoteModule;
import at.tugraz.tc.cyfile.note.NoteService;
import at.tugraz.tc.cyfile.secret.SecretManager;
import at.tugraz.tc.cyfile.secret.SecretModule;
import at.tugraz.tc.cyfile.secret.SecretPrompter;
import at.tugraz.tc.cyfile.ui.DisplayNoteActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.hasChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by kina on 11.04.18.
 */
public class MainActivityInstrumentedTest extends BaseInstrumentedTest {
    @Mock
    private NoteService noteService;

    @Mock
    private SecretManager secretManager;

    @Mock
    private KeyVaultService keyVaultService;

    @Mock
    private SecretPrompter secretPrompter;

    @Rule
    public IntentsTestRule<MainActivity> mActivityRule =
            new IntentsTestRule<>(MainActivity.class, true, false);


    @Before
    public void init() {
        ApplicationComponent applicationComponent
                = DaggerApplicationComponent.builder()
                .noteModule(new NoteModule(noteService))
                .asyncModule(new AsyncModule(mock(Executor.class)))
                .secretModule(new SecretModule(secretManager, secretPrompter,
                        new DummyKeyVaultService()))
                .appModule(new AppModule(app, new NoOpLogger()))
                .build();

        app.setComponent(applicationComponent);

        when(secretManager.verify(any()))
                .thenReturn(true);
    }

    @Test
    public void testNoNotesPresent() throws Exception {
        when(noteService.findAll())
                .thenReturn(Collections.emptyList());

        mActivityRule.launchActivity(new Intent());

        onView(withId(R.id.noteList))
                .check(ViewAssertions.matches(hasChildCount(0)));


    }

    private void mockRepo(List<Note> testNotes) {
        when(noteService.findAll())
                .thenReturn(testNotes);

        for (Note note : testNotes) {
            when(noteService.findOne(note.getId()))
                    .thenReturn(note);
        }
    }


    @Test
    public void testTwoNotesPresent() throws Exception {
        mockRepo(Arrays.asList(new Note("1", "name1", "content1")
                , new Note("2", "name2", "content2")));

        mActivityRule.launchActivity(new Intent());

        onView(withId(R.id.noteList))
                .check(ViewAssertions.matches(hasChildCount(2)));
    }

    @Test
    public void buttonsTest() throws Exception {

        when(noteService.findAll())
                .thenReturn(Arrays.asList(new Note("1", "name1", "content1")
                        , new Note("2", "name2", "content2")));

        when(noteService.findOne("1"))
                .thenReturn(new Note("1", "name1", "content1"));

        mActivityRule.launchActivity(new Intent());

        onView(withText("name1"))
                .check(ViewAssertions.matches(isDisplayed()));

        onView(withText("name1"))
                //TODO: check why this fails
                //.perform(scrollTo())
                .perform(click());

        onView(withId(R.id.noteList))
                .check(ViewAssertions.doesNotExist());
        intended(hasComponent(DisplayNoteActivity.class.getName()));
    }


    @Test
    public void testEditNote() {
        List<Note> testNotes = Arrays.asList(new Note("1", "name1", "content1")
                , new Note("2", "name2", "content2"));
        mockRepo(testNotes);

        mActivityRule.launchActivity(new Intent());

        String title = testNotes.get(0).getTitle();
        onView(withText(title))
                .check(ViewAssertions.matches(isDisplayed()));

        onView(withText(title))
                //TODO: check why this fails
                //.perform(scrollTo())
                .perform(click());

        onView(withId(R.id.noteList))
                .check(ViewAssertions.doesNotExist());

        intended(hasComponent(DisplayNoteActivity.class.getName()));
    }
}