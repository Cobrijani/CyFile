package at.tugraz.tc.cyfile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.List;

import javax.inject.Inject;

import at.tugraz.tc.cyfile.domain.Note;
import at.tugraz.tc.cyfile.note.NoteService;


/**
 * Main activity
 * Created by cobri on 3/21/2018.
 */
public class MainActivity extends AppCompatActivity {

    @Inject
    NoteService noteService;

    public static final String NOTE_ID = "NOTE_ID";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((CyFileApplication) getApplication()).getNoteComponent().inject(this);

        loadNoteList();
    }

    private void loadNoteList() {
       final  LinearLayout linear = (LinearLayout) findViewById(R.id.noteList);

        for (Note note :
                noteService.findAll()) {
            Button btn = new Button(this);
            btn.setTextSize(11);
            btn.setText(note.getTitle());
            btn.setHeight(70);
            btn.setWidth(linear.getWidth());
            btn.setTag(note.getId());
            btn.setOnClickListener(view -> openNoteInDetailActivity(note.getId()));
            linear.addView(btn);
        }
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
}
