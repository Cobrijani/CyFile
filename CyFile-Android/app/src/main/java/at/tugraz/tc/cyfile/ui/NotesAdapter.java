package at.tugraz.tc.cyfile.ui;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import at.tugraz.tc.cyfile.R;
import at.tugraz.tc.cyfile.domain.Note;
import co.dift.ui.SwipeToAction;


public class NotesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Note> items;


    /** References to the views for each data item **/
    public class NoteViewHolder extends SwipeToAction.ViewHolder<Note> {
        public TextView titleView;
        public TextView contentView;

        public NoteViewHolder(View v) {
            super(v);

            titleView = (TextView) v.findViewById(R.id.title);
            contentView = (TextView) v.findViewById(R.id.content);
        }
    }

    /** Constructor **/
    public NotesAdapter(List<Note> items) {
        this.items = items;
    }

    public void updateData(List<Note> notes){
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

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view, parent, false);

        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Note item = items.get(position);
        NoteViewHolder vh = (NoteViewHolder) holder;
        vh.titleView.setText(item.getTitle());
        vh.contentView.setText(item.getContent());
        vh.data = item;
    }
}