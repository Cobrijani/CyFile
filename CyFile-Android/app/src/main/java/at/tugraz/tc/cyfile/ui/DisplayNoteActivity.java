package at.tugraz.tc.cyfile.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import at.tugraz.tc.cyfile.MainActivity;
import at.tugraz.tc.cyfile.R;
import at.tugraz.tc.cyfile.domain.Note;
import at.tugraz.tc.cyfile.logging.CyFileLogger;
import at.tugraz.tc.cyfile.note.NoteService;

public class DisplayNoteActivity extends BaseActivity {

    @Inject
    NoteService noteService;

    @Inject
    CyFileLogger logger;

    private Note loadedNote;
    private TextView textContent;
    private TextView textTitle;
    private TextView textModifiedDate;

    private boolean newNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_note);

        getActivityComponent().inject(this);

        initView();

        Intent intent = getIntent();
        String noteId = intent.getStringExtra(MainActivity.NOTE_ID);
        loadNote(noteId);
        newNote = false;
        if (loadedNote.getTitle() == null) {
            newNote = true;
            greyOutDeleteButton();
        }

        onOpenNote();
    }

    @Override
    public void onBackPressed() {
        boolean something_changed = false;
        if ((newNote && (!textContent.getText().toString().equals("") || !textTitle.getText().toString().equals(""))) ||
                (!newNote && (!textContent.getText().toString().equals(loadedNote.getContent()) ||
                        !textTitle.getText().toString().equals(loadedNote.getTitle())))) {
            something_changed = true;
        }
        if (!something_changed) {
            finish();
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(DisplayNoteActivity.this).create();
            alertDialog.setTitle(getResources().getString(R.string.back_confirmation_title));
            alertDialog.setMessage(getResources().getString(R.string.back_confirmation_content));
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.no),
                    (dialog, which) -> dialog.dismiss());
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                    (dialog, which) -> finish());
            alertDialog.show();
        }

    }

    private void greyOutDeleteButton() {
        FloatingActionButton deleteButton = findViewById(R.id.BTN_DEL);
        deleteButton.setBackgroundTintList(DisplayNoteActivity.this.getResources().getColorStateList(R.color.divider));
        deleteButton.setClickable(false);
    }

    private void initView() {
        textContent = findViewById(R.id.NOTE_CONTENT);
        textTitle = findViewById(R.id.NOTE_TITLE);
        textModifiedDate = findViewById(R.id.NOTE_MODIFIED);
    }

    public void loadNote(String noteId) {
        if (noteId == null) {
            loadedNote = new Note();
        } else {
            loadedNote = noteService.findOne(noteId);
        }
    }

    public void onOpenNote() {
        logger.d("Note Id", loadedNote.getId() + " ");
        logger.d("Note Content", loadedNote.getContent() + " ");
        logger.d("Note Modified", loadedNote.getDateTimeModified() + " ");

        textContent.setText(loadedNote.getContent());
        textTitle.setText(loadedNote.getTitle());

        if (loadedNote.getDateTimeModified() == null) {
            textModifiedDate.setVisibility(View.INVISIBLE);
        } else {

            DateFormat format = SimpleDateFormat.getDateTimeInstance();
            textModifiedDate.setText(format.format(new Date(loadedNote.getDateTimeModified())));
        }

    }

    public void onSelectSaveNote(View v) {
        String noteTitle = textTitle.getText().toString();
        String noteContent = textContent.getText().toString();
        logger.d("onSelectSaveNote", "Title:- " + noteTitle);
        logger.d("onSelectSaveNote", "Content:- " + noteContent);


        loadedNote.setTitle(noteTitle);
        loadedNote.setContent(noteContent);

        loadedNote = noteService.save(loadedNote);
        finish();
    }

    public void onSelectDeleteNote(View v) {
        AlertDialog alertDialog = new AlertDialog.Builder(DisplayNoteActivity.this).create();
        alertDialog.setTitle(getResources().getString(R.string.delete_confirmation_title));
        alertDialog.setMessage(getResources().getString(R.string.delete_confirmation_content)
                + " \"" + loadedNote.getTitle() + "\"?");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = getIntent();
                        String noteId = intent.getStringExtra(MainActivity.NOTE_ID);
                        noteService.delete(noteId);
                        finish();
                    }
                });
        alertDialog.show();
    }
}
