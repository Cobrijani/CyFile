package at.tugraz.tc.cyfile.ui

import android.app.AlertDialog
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.view.View
import android.widget.TextView
import at.tugraz.tc.cyfile.MainActivity
import at.tugraz.tc.cyfile.R
import at.tugraz.tc.cyfile.domain.Note
import at.tugraz.tc.cyfile.logging.CyFileLogger
import at.tugraz.tc.cyfile.note.NoteService
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class DisplayNoteActivity : BaseActivity() {

    @Inject
    lateinit var noteService: NoteService

    @Inject
    lateinit var logger: CyFileLogger

    private lateinit var loadedNote: Note
    private lateinit var textContent: TextView
    private lateinit var textTitle: TextView
    private lateinit var textModifiedDate: TextView

    private lateinit var btnSave: FloatingActionButton
    private lateinit var btnDel: FloatingActionButton

    private var newNote: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_note)
        activityComponent.inject(this)
        initView()
        val intent = intent
        val noteId = intent.getStringExtra(MainActivity.NOTE_ID)
        loadNote(noteId)
        newNote = false
        val scrollView = findViewById<View>(R.id.SCROLL_VIEW)
        val noteContent = findViewById<View>(R.id.NOTE_CONTENT)
        noteContent.requestFocus()
        if (noteId == null) {
            newNote = true
            greyOutDeleteButton()
            hideDateModified()
        }
        onOpenNote()
        scrollView.scrollTo(0, noteContent.top)

        btnSave.setOnClickListener { onSelectSaveNote() }
        btnDel.setOnClickListener { onSelectDeleteNote() }
    }

    private fun hideDateModified() {
        val dateModified = findViewById<View>(R.id.NOTE_MODIFIED)
        dateModified.visibility = View.INVISIBLE
    }

    override fun onBackPressed() {
        var somethingChanged = false
        if (newNote &&
                (textContent.text.toString() != ""
                        || textTitle.text.toString() != "")
                || !newNote && (textContent.text.toString() != loadedNote.content
                        || textTitle.text.toString() != loadedNote.title)) {
            somethingChanged = true
        }
        if (!somethingChanged) {
            finish()
        } else {
            val alertDialog = AlertDialog.Builder(this@DisplayNoteActivity).create()
            alertDialog.setTitle(resources.getString(R.string.back_confirmation_title))
            alertDialog.setMessage(resources.getString(R.string.back_confirmation_content))
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, resources.getString(R.string.no)
            ) { dialog, _ -> dialog.dismiss() }
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, resources.getString(R.string.yes)
            ) { _, _ -> finish() }
            alertDialog.show()
        }

    }

    private fun greyOutDeleteButton() {
        btnDel.backgroundTintList = this@DisplayNoteActivity.getColorStateList(R.color.divider)
        btnDel.isClickable = false
    }

    private fun initView() {
        textContent = findViewById(R.id.NOTE_CONTENT)
        textTitle = findViewById(R.id.NOTE_TITLE)
        textModifiedDate = findViewById(R.id.NOTE_MODIFIED)

        btnSave = findViewById(R.id.BTN_SAVE)
        btnDel = findViewById(R.id.BTN_DEL)
    }

    private fun loadNote(noteId: String?) {
        loadedNote = if (noteId == null) {
            Note("", "", "")
        } else {
            noteService.findOne(noteId)!!
        }
    }

    private fun onOpenNote() {
        logger.d("Note Id", loadedNote.id + " ")
        logger.d("Note Content", loadedNote.content + " ")
        logger.d("Note Modified", loadedNote.dateTimeModified!!.toString() + " ")

        textContent.text = loadedNote.content
        textTitle.text = loadedNote.title

        if (loadedNote.dateTimeModified == null) {
            textModifiedDate.visibility = View.INVISIBLE
        } else {

            val format = SimpleDateFormat.getDateTimeInstance()
            textModifiedDate.text = format.format(Date(loadedNote.dateTimeModified!!))
        }

    }

    private fun onSelectSaveNote() {
        val noteTitle = textTitle.text.toString()
        val noteContent = textContent.text.toString()
        logger.d("onSelectSaveNote", "Title:- $noteTitle")
        logger.d("onSelectSaveNote", "Content:- $noteContent")


        loadedNote.title = noteTitle
        loadedNote.content = noteContent

        loadedNote = noteService.save(loadedNote)
        finish()
    }

    private fun onSelectDeleteNote() {
        val alertDialog = AlertDialog.Builder(this@DisplayNoteActivity).create()
        alertDialog.setTitle(resources.getString(R.string.delete_confirmation_title))
        alertDialog.setMessage(resources.getString(R.string.delete_confirmation_content)
                + " \"" + loadedNote.title + "\"?")
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, resources.getString(R.string.no)
        ) { dialog, _ -> dialog.dismiss() }
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, resources.getString(R.string.yes)
        ) { _, _ ->
            val intent = intent
            val noteId = intent.getStringExtra(MainActivity.NOTE_ID)
            noteService.delete(noteId)
            finish()
        }
        alertDialog.show()
    }
}
