package at.tugraz.tc.cyfile;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.internal.matchers.Null;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static android.support.test.espresso.matcher.ViewMatchers.hasChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.mock;

import at.tugraz.tc.cyfile.crypto.NoOpCryptoService;
import at.tugraz.tc.cyfile.domain.Note;
import at.tugraz.tc.cyfile.note.DaggerNoteComponent;
import at.tugraz.tc.cyfile.note.InMemoryNoteRepository;
import at.tugraz.tc.cyfile.note.NoteComponent;
import at.tugraz.tc.cyfile.note.NoteModule;
import at.tugraz.tc.cyfile.note.NoteService;
import at.tugraz.tc.cyfile.note.SecureNoteService;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Mockito.when;

/**
 * Created by kina on 11.04.18.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentedTest {

    private NoteService noteService;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<MainActivity>(MainActivity.class, true , false);


    @Before
    public void init(){
        CyFileApplication app = (CyFileApplication)
                InstrumentationRegistry
                        .getInstrumentation()
                .getTargetContext()
                .getApplicationContext();

        noteService = mock(NoteService.class);

        NoteComponent mockedComponent = DaggerNoteComponent
                .builder()
                .noteModule(new NoteModule(noteService))
                .build();

        app.setmNoteComponent(mockedComponent);
    }

    @Test
    public void testNoNotesPresent() throws Exception{
        when(noteService.findAll())
                .thenReturn(Collections.emptyList());

        mActivityRule.launchActivity(new Intent());

        onView(withId(R.id.noteList))
                .check(ViewAssertions.matches(hasChildCount(0)));


    }

    @Test
    public void testTwoNotesPresent() throws Exception{
        when(noteService.findAll())
                .thenReturn(Arrays.asList(new Note("1", "name1", "content1")
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

        onView(withText("name1")).perform(click());
            onView(withId(R.id.noteList))
                    .check(ViewAssertions.doesNotExist());
    }
}