package at.tugraz.tc.cyfile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import javax.inject.Inject;

import at.tugraz.tc.cyfile.domain.Note;
import at.tugraz.tc.cyfile.logging.CyFileLogger;
import at.tugraz.tc.cyfile.note.NoteService;
import at.tugraz.tc.cyfile.secret.SecretPrompter;
import at.tugraz.tc.cyfile.ui.BaseActivity;
import at.tugraz.tc.cyfile.ui.DisplayNoteActivity;


/**
 * Main activity
 * Created by cobri on 3/21/2018.
 */
public class MainActivity extends BaseActivity {

    @Inject
    NoteService noteService;

    @Inject
    SecretPrompter secretPrompter;

    @Inject
    CyFileLogger logger;

    public static final String NOTE_ID = "NOTE_ID";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActivityComponent().inject(this);
        secretPrompter.promptSecret();
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadNoteList();
    }

    private void loadNoteList() {
        final LinearLayout linear = findViewById(R.id.noteList);
        linear.removeAllViews();

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

    ;
}
