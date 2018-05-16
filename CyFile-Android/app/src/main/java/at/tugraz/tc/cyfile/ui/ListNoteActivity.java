package at.tugraz.tc.cyfile.ui;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import javax.inject.Inject;

import at.tugraz.tc.cyfile.MainActivity;
import at.tugraz.tc.cyfile.R;
import at.tugraz.tc.cyfile.domain.Note;
import at.tugraz.tc.cyfile.note.NoteService;
import at.tugraz.tc.cyfile.secret.SecretPrompter;
import co.dift.ui.SwipeToAction;

public class ListNoteActivity extends BaseActivity  {

    @Inject
    NoteService noteService;

    @Inject
    SecretPrompter secretPrompter;

    private RecyclerView recyclerView;
    private NotesAdapter adapter;
    private SwipeToAction swipeToAction;

    public static final String NOTE_ID = "NOTE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_note);

        Log.d("ListNoteActivity", "Inside List Note Activity");

        getActivityComponent().inject(this);

        initializeNoteView();
    }

    private void hideApp() {
        PackageManager p = getPackageManager();
        ComponentName componentName = new ComponentName(this, MainActivity.class);
        p.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    private void displayApp() {
        PackageManager p = getPackageManager();
        ComponentName componentName = new ComponentName(this, MainActivity.class);
        p.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    protected void initializeNoteView() {
        recyclerView = findViewById(R.id.noteList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new NotesAdapter(noteService.findAll());
        recyclerView.setAdapter(adapter);

        swipeToAction = new SwipeToAction(recyclerView, new SwipeToAction.SwipeListener<Note>() {
            @Override
            public boolean swipeLeft(final Note itemData) {
                hideApp();
                return true;
            }

            @Override
            public boolean swipeRight(Note itemData) {
                displayApp();
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

    public void onSelectDeleteNote(String noteId) {
        Log.d("onSelectDeleteNote", "on select delete note: " + noteId);


        noteService.delete(noteId);

    }

    private void displaySnackbar(String text, String actionName, View.OnClickListener action) {
        Snackbar snack = Snackbar.make(findViewById(android.R.id.content), text, Snackbar.LENGTH_LONG)
                .setAction(actionName, action);

        View v = snack.getView();
        v.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        ((TextView) v.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(Color.WHITE);
        ((TextView) v.findViewById(android.support.design.R.id.snackbar_action))
                .setTextColor(getResources().getColor(R.color.colorDeleteButton));

        snack.show();
    }



}
