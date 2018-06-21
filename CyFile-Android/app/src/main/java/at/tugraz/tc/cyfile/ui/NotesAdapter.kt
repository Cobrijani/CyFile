package at.tugraz.tc.cyfile.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import at.tugraz.tc.cyfile.R
import at.tugraz.tc.cyfile.domain.Note
import co.dift.ui.SwipeToAction
import java.text.SimpleDateFormat
import java.util.*


class NotesAdapter
/**
 * Constructor
 */
(private var items: List<Note>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun filter(newText: String, notesList: List<Note>) {
        val notes = ArrayList<Note>()
        for (note in notesList) {
            if (note.content!!.contains(newText) || note.title!!.contains(newText)) {
                notes.add(note)
            }
        }
        updateData(notes)
    }


    /**
     * References to the views for each data item
     */
    inner class NoteViewHolder internal constructor(v: View) : SwipeToAction.ViewHolder<Note>(v) {
        internal var titleView: TextView = v.findViewById(R.id.title)
        internal var contentView: TextView = v.findViewById(R.id.content)
        internal var modifiedView: TextView = v.findViewById(R.id.modified)

    }

    fun updateData(notes: List<Note>) {
        this.items = notes
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    override fun getItemCount(): Int {
        return items!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_view, parent, false)

        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items!![position]
        val vh = holder as NoteViewHolder
        vh.titleView.text = item.title
        vh.contentView.text = item.content
        if (item.dateTimeModified == null) {
            vh.modifiedView.visibility = View.INVISIBLE
        } else {
            val format = SimpleDateFormat.getDateTimeInstance()
            vh.modifiedView.text = format.format(Date(item.dateTimeModified!!))
        }

        vh.data = item
    }
}