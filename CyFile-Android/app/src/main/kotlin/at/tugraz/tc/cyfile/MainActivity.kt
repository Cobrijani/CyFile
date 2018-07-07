package at.tugraz.tc.cyfile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import at.tugraz.tc.cyfile.crypto.exceptions.KeyVaultLockedException
import at.tugraz.tc.cyfile.crypto.exceptions.KeyVaultNotInitializedException
import at.tugraz.tc.cyfile.domain.Note
import at.tugraz.tc.cyfile.logging.CyFileLogger
import at.tugraz.tc.cyfile.note.NoteService
import at.tugraz.tc.cyfile.secret.SecretPrompter
import at.tugraz.tc.cyfile.ui.BaseActivity
import at.tugraz.tc.cyfile.ui.DisplayNoteActivity
import at.tugraz.tc.cyfile.ui.NotesAdapter
import at.tugraz.tc.cyfile.ui.SettingsActivity
import co.dift.ui.SwipeToAction
import java.util.*
import javax.inject.Inject


/**
 * Main activity
 * Created by cobri on 3/21/2018.
 */
class MainActivity : BaseActivity(), SearchView.OnQueryTextListener {

    @Inject
    lateinit var noteService: NoteService

    @Inject
    lateinit var secretPrompter: SecretPrompter

    @Inject
    lateinit var logger: CyFileLogger

    private var adapter: NotesAdapter = NotesAdapter(emptyList())

    private lateinit var searchView: SearchView

    private lateinit var btnAdd: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activityComponent.inject(this)
        secretPrompter.promptSecret()
        initializeNoteView()
        btnAdd = findViewById(R.id.BTN_ADD)
        searchView = findViewById(R.id.search_note)
        searchView.setOnQueryTextListener(this)

        btnAdd.setOnClickListener { onSelectAddNote() }
    }

    private fun initializeNoteView() {
        val recyclerView = findViewById<RecyclerView>(R.id.noteList)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)

        recyclerView.adapter = adapter
        SwipeToAction(recyclerView, object : SwipeToAction.SwipeListener<Note> {
            override fun swipeLeft(itemData: Note): Boolean {
                onSelectDeleteNote(itemData.id)
                return true
            }

            override fun swipeRight(itemData: Note): Boolean {
                openNoteInDetailActivity(itemData.id)
                return true
            }

            override fun onClick(itemData: Note) {
                openNoteInDetailActivity(itemData.id)
            }

            override fun onLongClick(itemData: Note) {
                openNoteInDetailActivity(itemData.id)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.action_favorite, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
        /*case R.id.action_search:
                // User chose the "Settings" item, show the app settings UI...
                Log.d("onSearchNotes", "on select search notes");
                return true;

            case R.id.action_favorite:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Log.d("onSelectMarkAsFavorite", "on select mark as favorite");
                return true; */

            R.id.action_settings -> {
                Log.d("onSelectSettings", "on select open settings")
                openSettingsActivity()
                return true
            }
            else ->
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item)
        }
    }

    private fun openSettingsActivity() {
        logger.d("onSelectSettings", "Go to settings")

        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }


    override fun onResume() {
        super.onResume()
        updateNoteList()
    }

    private fun updateNoteList() {
        try {
            adapter.updateData(noteService.findAll())
        } catch (e: KeyVaultLockedException) {
            e.printStackTrace()
        } catch (e: KeyVaultNotInitializedException) {
            e.printStackTrace()
        }

    }

    private fun openNoteInDetailActivity(noteId: String) {
        val noteMessage = noteService.findOne(noteId)!!
        logger.d("Note Id", noteMessage.id)
        logger.d("Note Content", Objects.requireNonNull<String>(noteMessage.content))

        val intent = Intent(this, DisplayNoteActivity::class.java)

        val message = noteMessage.id
        intent.putExtra(NOTE_ID, message)
        startActivity(intent)
    }

    private fun onSelectAddNote() {
        logger.d("onSelectAddNote", "on select add new note")
        val intent = Intent(this, DisplayNoteActivity::class.java)
        startActivity(intent)
    }

    fun onSelectDeleteNote(noteId: String) {
        Log.d("onSelectDeleteNote", "on select delete note: $noteId")

        val alertDialog = AlertDialog.Builder(this@MainActivity).create()
        alertDialog.setTitle(resources.getString(R.string.delete_confirmation_title))
        alertDialog.setMessage(resources.getString(R.string.delete_confirmation_content)
                + " \"" + noteService.findOne(noteId)!!.title + "\"?")
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, resources.getString(R.string.no)
        ) { dialog, _ -> dialog.dismiss() }
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, resources.getString(R.string.yes)
        ) { dialog, _ ->
            noteService.delete(noteId)
            updateNoteList()
            dialog.dismiss()
        }
        alertDialog.show()
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        adapter.filter(newText, noteService.findAll())
        return false
    }

    companion object {
        const val NOTE_ID = "NOTE_ID"
    }
}
