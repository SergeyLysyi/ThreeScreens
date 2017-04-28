package sergeylysyi.threescreens;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class NoteListAdapter extends ArrayAdapter {
    private final LayoutInflater inflater;
    private final int resource;

    public NoteListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Note> objects) {
        super(context, resource, objects);
        inflater = LayoutInflater.from(getContext());
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(resource, parent, false);
            holder = new ViewHolder();
            holder.header = ((TextView) convertView.findViewById(R.id.header));
            holder.body = ((TextView) convertView.findViewById(R.id.body));
            holder.rectangle = ((ImageView) convertView.findViewById(R.id.color));

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) v.getContext()).editNote((Note) getItem(holder.position));
                }
            });
            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ((MainActivity) v.getContext()).deleteNote((Note) getItem(holder.position));
                    return true;
                }
            });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Note note = (Note) getItem(position);
        holder.position = position;
        holder.header.setText(note.getHeader());
        holder.body.setText(note.getBody());
        holder.rectangle.setBackgroundColor(note.getColor());

        return convertView;
    }

    private static class ViewHolder {
        int position;
        TextView header;
        TextView body;
        ImageView rectangle;
    }
}
