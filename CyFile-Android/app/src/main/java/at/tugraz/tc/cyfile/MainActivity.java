package at.tugraz.tc.cyfile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import at.tugraz.tc.cyfile.domain.Note;
import at.tugraz.tc.cyfile.note.NoteService;
import at.tugraz.tc.cyfile.secret.SecretPrompter;
import at.tugraz.tc.cyfile.ui.BaseActivity;
import at.tugraz.tc.cyfile.ui.DisplayNoteActivity;
import at.tugraz.tc.cyfile.ui.NotesAdapter;
import co.dift.ui.SwipeToAction;


/**
 * Main activity
 * Created by cobri on 3/21/2018.
 */
public class MainActivity extends BaseActivity {

    @Inject
    NoteService noteService;

    @Inject
    SecretPrompter secretPrompter;

    private RecyclerView recyclerView;
    private NotesAdapter adapter;
    private SwipeToAction swipeToAction;

    public static final String NOTE_ID = "NOTE_ID";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActivityComponent().inject(this);
        secretPrompter.promptSecret();
        initializeNoteView();
    }

    protected void initializeNoteView()
    {
        recyclerView = (RecyclerView) findViewById(R.id.noteList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new NotesAdapter(noteService.findAll());
        recyclerView.setAdapter(adapter);

        swipeToAction = new SwipeToAction(recyclerView, new SwipeToAction.SwipeListener<Note>() {
            @Override
            public boolean swipeLeft(final Note itemData) {
                return true;
            }

            @Override
            public boolean swipeRight(Note itemData) {
                return true;
            }

            @Override
            public void onClick(Note itemData) {
                openNoteInDetailActivity(itemData.getId());
            }

            @Override
            public void onLongClick(Note itemData) {
                openNoteInDetailActivity(itemData.getId());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateNoteList();
    }

    private void updateNoteList() {
        adapter.updateData(noteService.findAll());
    }


    private void openNoteInDetailActivity(String noteId) {
        Note noteMessage = noteService.findOne(noteId);
        Log.d("Note Id", noteMessage.getId());
        Log.d("Note Content", noteMessage.getContent());

        Intent intent = new Intent(this, DisplayNoteActivity.class);

        String message = noteMessage.getId();
        intent.putExtra(NOTE_ID, message);
        startActivity(intent);
    }

    public void onSelectAddNote(View v) {
        Log.d("onSelectAddNote", "on select add new note");

        Intent intent = new Intent(this, DisplayNoteActivity.class);
        startActivity(intent);
    }
}
