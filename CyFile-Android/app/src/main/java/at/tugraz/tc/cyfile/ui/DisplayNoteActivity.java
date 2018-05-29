package at.tugraz.tc.cyfile.ui;

import android.content.Intent;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_note);

        getActivityComponent().inject(this);

        initView();

        Intent intent = getIntent();
        String noteId = intent.getStringExtra(MainActivity.NOTE_ID);
        loadNote(noteId);

        onOpenNote();
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
        Intent intent = getIntent();
        String noteId = intent.getStringExtra(MainActivity.NOTE_ID);

        noteService.delete(noteId);

        finish();
    }
}
