package at.tugraz.tc.cyfile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import javax.inject.Inject;

import at.tugraz.tc.cyfile.domain.Note;
import at.tugraz.tc.cyfile.note.NoteService;

public class DisplayNoteActivity extends AppCompatActivity {

    @Inject
    NoteService noteService;

    private Note loadedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_note);

        ((CyFileApplication) getApplication()).getNoteComponent().inject(this);

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
        Log.d("Note Id", loadedNote.getId() + " " );
        Log.d("Note Content", loadedNote.getContent() + " " );

        TextView textView = findViewById(R.id.noteText);
        textView.setText(loadedNote.getContent());

        TextView textTitle = findViewById(R.id.TEXT_TITLE);
        textTitle.setText(loadedNote.getTitle());
    }

    public void onSelectSaveNote(View v) {
        TextView textTitle = findViewById(R.id.TEXT_TITLE);
        TextView textView = findViewById(R.id.noteText);

        String noteTitle = textTitle.getText().toString();
        String noteContent = textView.getText().toString();
        Log.d("onSelectSaveNote", "Title:- " + noteTitle);
        Log.d("onSelectSaveNote", "Content:- " + noteContent);

        loadedNote.setTitle(noteTitle);
        loadedNote.setContent(noteContent);

        loadedNote = noteService.save(loadedNote);
    };
}
