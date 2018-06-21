package at.tugraz.tc.cyfile;

import android.content.Intent;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.GeneralLocation;
import android.support.test.espresso.action.GeneralSwipeAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Swipe;
import android.support.test.espresso.assertion.PositionAssertions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

import at.tugraz.tc.cyfile.injection.DaggerApplicationComponent;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
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
    public void testSearchNotes() {
        mockRepo(Arrays.asList(new Note("1", "name1", "content1", new Date().getTime() - 100, new Date().getTime() - 100)
                , new Note("2", "name2", "content2", new Date().getTime() + 100, new Date().getTime() + 100),
                new Note("3", "name3", "content3", new Date().getTime(), new Date().getTime())));


        mActivityRule.launchActivity(new Intent());

        onView(withId(R.id.search_button))
                .perform(click());

        onView(withId(R.id.search_src_text))
                .perform(typeText("name3"));

        onView(withText("content1"))
                .check(doesNotExist());

        onView(withText("content2"))
                .check(doesNotExist());

        onView(withText("content3"))
                .check(matches(isDisplayed()));

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
    @Ignore
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
                .perform(new GeneralSwipeAction(Swipe.SLOW, GeneralLocation.CENTER_LEFT,
                        GeneralLocation.CENTER_RIGHT, Press.THUMB));


        onView(withText("Are you sure you want to delete")).check(matches(isDisplayed()));

    }

    @Test
    public void settingsTest() throws Exception {

        mActivityRule.launchActivity(new Intent());

        onView(withId(R.id.action_settings)).perform(click());

        intended(hasComponent(SettingsActivity.class.getName()));
    }

}