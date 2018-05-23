package at.tugraz.tc.cyfile.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import javax.inject.Inject;

import at.tugraz.tc.cyfile.MainActivity;
import at.tugraz.tc.cyfile.R;
import at.tugraz.tc.cyfile.domain.Note;
import at.tugraz.tc.cyfile.note.NoteService;

public class DisplayNoteActivity extends BaseActivity {

    @Inject
    NoteService noteService;

    private Note loadedNote;
    private TextView textContent;
    private TextView textTitle;

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
        if (loadedNote.getTitle() == null)
        {
            newNote = true;
            greyOutDeleteButton();
        }

        onOpenNote();
    }

    @Override
    public void onBackPressed() {
        boolean something_changed = false;
        boolean a = !textContent.getText().toString().equals("");
        boolean b = !textTitle.getText().toString().equals("");
        if ((newNote && (!textContent.getText().toString().equals("") || !textTitle.getText().toString().equals(""))) ||
                (!newNote && (!textContent.getText().toString().equals(loadedNote.getContent()) ||
                        !textTitle.getText().toString().equals(loadedNote.getTitle()))))
        {
            something_changed = true;
        }
        if (!something_changed)
        {
            finish();
        }
        else
        {
            AlertDialog alertDialog = new AlertDialog.Builder(DisplayNoteActivity.this).create();
            alertDialog.setTitle(getResources().getString(R.string.back_confirmation_title));
            alertDialog.setMessage(getResources().getString(R.string.back_confirmation_content));
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.no),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            alertDialog.show();
        }

    }

    private void greyOutDeleteButton() {
        FloatingActionButton deleteButton = findViewById(R.id.BTN_DEL);
        deleteButton.setBackgroundTintList(DisplayNoteActivity.this.getResources().getColorStateList(R.color.colorGreyedOutDeleteButton));
        deleteButton.setClickable(false);
    }

    private void initView() {
        textContent = findViewById(R.id.NOTE_CONTENT);
        textTitle = findViewById(R.id.NOTE_TITLE);
    }

    public void loadNote(String noteId) {
        if (noteId == null) {
            loadedNote = new Note();
        } else {
            loadedNote = noteService.findOne(noteId);
        }
    }

    public void onOpenNote() {
        Log.d("Note Id", loadedNote.getId() + " ");
        Log.d("Note Content", loadedNote.getContent() + " ");

        textContent.setText(loadedNote.getContent());
        textTitle.setText(loadedNote.getTitle());
    }

    public void onSelectSaveNote(View v) {
        String noteTitle = textTitle.getText().toString();
        String noteContent = textContent.getText().toString();
        Log.d("onSelectSaveNote", "Title:- " + noteTitle);
        Log.d("onSelectSaveNote", "Content:- " + noteContent);



        loadedNote.setTitle(noteTitle);
        loadedNote.setContent(noteContent);

        loadedNote = noteService.save(loadedNote);
        finish();
    }

    public void onSelectDeleteNote(View v) {
        AlertDialog alertDialog = new AlertDialog.Builder(DisplayNoteActivity.this).create();
        alertDialog.setTitle(getResources().getString(R.string.delete_confirmation_title));
        alertDialog.setMessage(getResources().getString(R.string.delete_confirmation_content)
                +" \"" + loadedNote.getTitle() + "\"?");
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
