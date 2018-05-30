package at.tugraz.tc.cyfile.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import at.tugraz.tc.cyfile.R;
import at.tugraz.tc.cyfile.domain.Note;
import co.dift.ui.SwipeToAction;


public class NotesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Note> items;


    /**
     * References to the views for each data item
     **/
    public class NoteViewHolder extends SwipeToAction.ViewHolder<Note> {
        TextView titleView;
        TextView contentView;
        TextView modifiedView;

        NoteViewHolder(View v) {
            super(v);

            titleView = v.findViewById(R.id.title);
            contentView = v.findViewById(R.id.content);
            modifiedView = v.findViewById(R.id.modified);
        }
    }

    /**
     * Constructor
     **/
    public NotesAdapter(List<Note> items) {
        this.items = items;
    }

    public void updateData(List<Note> notes) {
        this.items = notes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view, parent, false);

        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Note item = items.get(position);
        NoteViewHolder vh = (NoteViewHolder) holder;
        vh.titleView.setText(item.getTitle());
        vh.contentView.setText(item.getContent());
        if (item.getDateTimeModified() == null) {
            vh.modifiedView.setVisibility(View.INVISIBLE);
        } else {
            DateFormat format = SimpleDateFormat.getDateTimeInstance();
            vh.modifiedView.setText(format.format(new Date(item.getDateTimeModified())));
        }

        vh.data = item;
    }
}