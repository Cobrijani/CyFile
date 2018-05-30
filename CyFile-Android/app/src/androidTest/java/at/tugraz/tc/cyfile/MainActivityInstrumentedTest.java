package at.tugraz.tc.cyfile;

import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.GeneralLocation;
import android.support.test.espresso.action.GeneralSwipeAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Swipe;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.PositionAssertions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

import at.tugraz.tc.cyfile.async.AsyncModule;
import at.tugraz.tc.cyfile.crypto.impl.DummyKeyVaultService;
import at.tugraz.tc.cyfile.crypto.impl.NoOpCryptoService;
import at.tugraz.tc.cyfile.domain.Note;
import at.tugraz.tc.cyfile.injection.ApplicationComponent;
import at.tugraz.tc.cyfile.injection.DaggerApplicationComponent;
import at.tugraz.tc.cyfile.logging.impl.NoOpLogger;
import at.tugraz.tc.cyfile.note.NoteModule;
import at.tugraz.tc.cyfile.note.NoteRepository;
import at.tugraz.tc.cyfile.note.impl.SecureNoteService;
import at.tugraz.tc.cyfile.secret.SecretManager;
import at.tugraz.tc.cyfile.secret.SecretModule;
import at.tugraz.tc.cyfile.secret.SecretPrompter;
import at.tugraz.tc.cyfile.ui.DisplayNoteActivity;
import at.tugraz.tc.cyfile.ui.SettingsActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
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
    private SecretManager secretManager;

    @Mock
    private NoteRepository noteRepository;
    @Mock
    private SecretPrompter secretPrompter;

    @Rule
    public IntentsTestRule<MainActivity> mActivityRule =
            new IntentsTestRule<>(MainActivity.class, true, false);


    @Before
    public void init() {
        ApplicationComponent applicationComponent
                = DaggerApplicationComponent.builder()
                .noteModule(new NoteModule(new SecureNoteService(noteRepository, new NoOpCryptoService())))
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
        when(noteRepository.findAll())
                .thenReturn(Collections.emptyList());

        mActivityRule.launchActivity(new Intent());

        onView(withId(R.id.noteList))
                .check(matches(hasChildCount(0)));


    }

    private void mockRepo(List<Note> testNotes) {
        when(noteRepository.findAll())
                .thenReturn(testNotes);

        for (Note note : testNotes) {
            when(noteRepository.findOne(note.getId()))
                    .thenReturn(note);
        }
    }


    @Test
    public void testNotesSortedByDateTime() {

        mockRepo(Arrays.asList(new Note("1", "name1", "content1", new Date().getTime() - 100, new Date().getTime() - 100)
                , new Note("2", "name2", "content2", new Date().getTime() + 100, new Date().getTime() + 100),
                new Note("3", "name3", "content3", new Date().getTime(), new Date().getTime())));


        mActivityRule.launchActivity(new Intent());


        onView(withText("name1"))
                .check(PositionAssertions.isCompletelyBelow(withText("name2")));
        onView(withText("name3"))
                .check(PositionAssertions.isCompletelyBelow(withText("name2")));
        onView(withText("name1"))
                .check(PositionAssertions.isCompletelyBelow(withText("name3")));


//        name2 -> name3 -> name1

    }


    @Test
    public void testTwoNotesPresent() throws Exception {
        mockRepo(Arrays.asList(new Note("1", "name1", "content1", 0L, 0L)
                , new Note("2", "name2", "content2", 0L, 0L)));

        mActivityRule.launchActivity(new Intent());

        onView(withId(R.id.noteList))
                .check(matches(hasChildCount(2)));
    }

    @Test
    public void buttonsTest() throws Exception {

        when(noteRepository.findAll())
                .thenReturn(Arrays.asList(new Note("1", "name1", "content1", 0L, 0L)
                        , new Note("2", "name2", "content2", 0L, 0L)));

        when(noteRepository.findOne("1"))
                .thenReturn(new Note("1", "name1", "content1", 0L, 0L));

        mActivityRule.launchActivity(new Intent());

        onView(withText("name1"))
                .check(matches(isDisplayed()));

        onView(withText("name1"))
                //TODO: check why this fails
                //.perform(scrollTo())
                .perform(click());

        onView(withId(R.id.noteList))
                .check(doesNotExist());
        intended(hasComponent(DisplayNoteActivity.class.getName()));
    }

    @Test
    public void testEditNote() {
        List<Note> testNotes = Arrays.asList(new Note("1", "name1", "content1", 0L, 0L)
                , new Note("2", "name2", "content2", 0L, 0L));
        mockRepo(testNotes);

        mActivityRule.launchActivity(new Intent());

        String title = testNotes.get(0).getTitle();
        onView(withText(title))
                .check(matches(isDisplayed()));

        onView(withText(title))
                //TODO: check why this fails
                //.perform(scrollTo())
                .perform(click());

        onView(withId(R.id.noteList))
                .check(doesNotExist());

        intended(hasComponent(DisplayNoteActivity.class.getName()));
    }

    private static ViewAction swipeFromTopToBottom() {
        return new GeneralSwipeAction(Swipe.FAST, GeneralLocation.CENTER_LEFT,
                GeneralLocation.CENTER_RIGHT, Press.FINGER);
    }

    @Test
    public void testDeleteDialogNoteSwipeLeft() {
        List<Note> testNotes = Arrays.asList(new Note("1", "name1", "content1", 0L, 0L)
                , new Note("2", "name2", "content2", 0L, 0L));
        mockRepo(testNotes);

        mActivityRule.launchActivity(new Intent());

        String title = testNotes.get(0).getTitle();
        String content = testNotes.get(0).getContent();
        onView(withText(title))
                .check(ViewAssertions.matches(isDisplayed()));

        onView(withText(content))
                .perform(swipeLeft());


        onView(withText("Are you sure you want to delete")).check(matches(isDisplayed()));

    }

    @Test
    public void settingsTest() throws Exception {

        mActivityRule.launchActivity(new Intent());

        onView(withId(R.id.action_settings)).perform(click());

        intended(hasComponent(SettingsActivity.class.getName()));
    }

}