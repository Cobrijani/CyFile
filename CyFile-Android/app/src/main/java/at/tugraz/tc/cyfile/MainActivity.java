package at.tugraz.tc.cyfile;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import javax.inject.Inject;

import at.tugraz.tc.cyfile.domain.Note;
import at.tugraz.tc.cyfile.note.NoteService;
import at.tugraz.tc.cyfile.secret.SecretPrompter;
import at.tugraz.tc.cyfile.ui.BaseActivity;
import at.tugraz.tc.cyfile.ui.DisplayNoteActivity;
import at.tugraz.tc.cyfile.ui.ListNoteActivity;
import at.tugraz.tc.cyfile.ui.NotesAdapter;
import co.dift.ui.SwipeToAction;


/**
 * Main activity
 * Created by cobri on 3/21/2018.
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActivityComponent().inject(this);

        Intent listNoteIntent = new Intent(this, ListNoteActivity.class);
        startActivity(listNoteIntent);
    }
}