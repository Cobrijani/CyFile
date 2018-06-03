package at.tugraz.tc.cyfile.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import javax.inject.Inject;

import at.tugraz.tc.cyfile.R;
import at.tugraz.tc.cyfile.crypto.exceptions.KeyVaultLockedException;
import at.tugraz.tc.cyfile.domain.Note;
import at.tugraz.tc.cyfile.logging.CyFileLogger;
import at.tugraz.tc.cyfile.note.NoteService;
import at.tugraz.tc.cyfile.secret.SecretPrompter;
import co.dift.ui.SwipeToAction;

public class NoteListActivity extends BaseActivity {

    @Inject
    NoteService noteService;

    @Inject
    SecretPrompter secretPrompter;

    @Inject
    CyFileLogger logger;

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
                onSelectDeleteNote(itemData.getId());
                return true;
            }

            @Override
            public boolean swipeRight(Note itemData) {
                openNoteInDetailActivity(itemData.getId());
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_favorite, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*case R.id.action_search:
                // User chose the "UserSettings" item, show the app settings UI...
                Log.d("onSearchNotes", "on select search notes");
                return true;

            case R.id.action_favorite:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Log.d("onSelectMarkAsFavorite", "on select mark as favorite");
                return true; */

            case R.id.action_settings:
                Log.d("onSelectSettings", "on select open settings");
                openSettingsActivity();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void openSettingsActivity() {
        logger.d("onSelectSettings", "Go to settings");

        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateNoteList();
    }

    private void updateNoteList() {
        try {
            adapter.updateData(noteService.findAll());
        } catch (KeyVaultLockedException e) {
            e.printStackTrace();
        }
    }

    private void openNoteInDetailActivity(String noteId) {
        Note noteMessage = noteService.findOne(noteId);
        logger.d("Note Id", noteMessage.getId());
        logger.d("Note Content", noteMessage.getContent());

        Intent intent = new Intent(this, DisplayNoteActivity.class);

        String message = noteMessage.getId();
        intent.putExtra(NOTE_ID, message);
        startActivity(intent);
    }

    public void onSelectAddNote(View v) {
        logger.d("onSelectAddNote", "on select add new note");

        Intent intent = new Intent(this, DisplayNoteActivity.class);
        startActivity(intent);
    }

    public void onSelectDeleteNote(String noteId) {
        Log.d("onSelectDeleteNote", "on select delete note: " + noteId);

        AlertDialog alertDialog = new AlertDialog.Builder(NoteListActivity.this).create();
        alertDialog.setTitle(getResources().getString(R.string.delete_confirmation_title));
        alertDialog.setMessage(getResources().getString(R.string.delete_confirmation_content)
                + " \"" + noteService.findOne(noteId).getTitle() + "\"?");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        noteService.delete(noteId);
                        updateNoteList();
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
