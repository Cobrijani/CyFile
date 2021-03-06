package at.tugraz.tc.cyfile;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.Collections;

import javax.inject.Inject;

import at.tugraz.tc.cyfile.crypto.exceptions.KeyVaultLockedException;
import at.tugraz.tc.cyfile.crypto.exceptions.KeyVaultNotInitializedException;
import at.tugraz.tc.cyfile.domain.Note;
import at.tugraz.tc.cyfile.logging.CyFileLogger;
import at.tugraz.tc.cyfile.note.NoteService;
import at.tugraz.tc.cyfile.secret.SecretPrompter;
import at.tugraz.tc.cyfile.ui.BaseActivity;
import at.tugraz.tc.cyfile.ui.DisplayNoteActivity;
import at.tugraz.tc.cyfile.ui.NotesAdapter;
import at.tugraz.tc.cyfile.ui.SettingsActivity;
import co.dift.ui.SwipeToAction;


/**
 * Main activity
 * Created by cobri on 3/21/2018.
 */
public class MainActivity extends BaseActivity implements SearchView.OnQueryTextListener {

    @Inject
    NoteService noteService;

    @Inject
    SecretPrompter secretPrompter;

    @Inject
    CyFileLogger logger;

    private NotesAdapter adapter;

    private SearchView searchView;

    public static final String NOTE_ID = "NOTE_ID";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActivityComponent().inject(this);
        secretPrompter.promptSecret();
        initializeNoteView();

        searchView = findViewById(R.id.search_note);

        searchView.setOnQueryTextListener(this);
    }

    protected void initializeNoteView() {

        RecyclerView recyclerView = findViewById(R.id.noteList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new NotesAdapter(Collections.emptyList());
        recyclerView.setAdapter(adapter);
        new SwipeToAction(recyclerView, new SwipeToAction.SwipeListener<Note>() {
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
                // User chose the "Settings" item, show the app settings UI...
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
        } catch (KeyVaultLockedException | KeyVaultNotInitializedException e) {
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

        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle(getResources().getString(R.string.delete_confirmation_title));
        alertDialog.setMessage(getResources().getString(R.string.delete_confirmation_content)
                + " \"" + noteService.findOne(noteId).getTitle() + "\"?");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.no),
                (dialog, which) -> dialog.dismiss());
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                (dialog, which) -> {
                    noteService.delete(noteId);
                    updateNoteList();
                    dialog.dismiss();
                });
        alertDialog.show();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filter(newText, noteService.findAll());
        return false;
    }
}
