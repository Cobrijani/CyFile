package at.tugraz.tc.cyfile.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import javax.inject.Inject;

import at.tugraz.tc.cyfile.MainActivity;
import at.tugraz.tc.cyfile.R;
import at.tugraz.tc.cyfile.domain.Note;
import at.tugraz.tc.cyfile.note.NoteService;

public class DisplayNoteActivity extends BaseActivity {

    @Inject
    NoteService noteService;

    private Note loadedNote;
    private TextView textView;
    private TextView textTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_note);

        getActivityComponent().inject(this);

        textView = findViewById(R.id.noteText);
        textTitle = findViewById(R.id.TEXT_TITLE);


        Intent intent = getIntent();
        String noteId = intent.getStringExtra(MainActivity.NOTE_ID);
        loadNote(noteId);

        onOpenNote();
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

        textView.setText(loadedNote.getContent());
        textTitle.setText(loadedNote.getTitle());
    }

    public void onSelectSaveNote(View v) {
        String noteTitle = textTitle.getText().toString();
        String noteContent = textView.getText().toString();
        Log.d("onSelectSaveNote", "Title:- " + noteTitle);
        Log.d("onSelectSaveNote", "Content:- " + noteContent);

        loadedNote.setTitle(noteTitle);
        loadedNote.setContent(noteContent);

        loadedNote = noteService.save(loadedNote);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
