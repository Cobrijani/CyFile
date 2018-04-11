package at.tugraz.tc.cyfile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import javax.inject.Inject;

import at.tugraz.tc.cyfile.domain.Note;
import at.tugraz.tc.cyfile.note.NoteService;

public class DisplayNoteActivity extends AppCompatActivity {

    @Inject
    NoteService noteService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_note);

        ((CyFileApplication) getApplication()).getNoteComponent().inject(this);

        Intent intent = getIntent();
        String noteId = intent.getStringExtra(MainActivity.NOTE_ID);

        Note currentNote = noteService.findOne(noteId);

        Log.d("Note Id", noteId);
        Log.d("Note Content", currentNote.getContent());

        String message = currentNote.getContent();
        TextView textView = findViewById(R.id.noteText);
        textView.setText(message);
    }
}
