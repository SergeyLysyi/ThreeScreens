package sergeylysyi.threescreens;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Note> notes;
    private NoteListAdapter noteListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("onCreate");
        setContentView(R.layout.activity_main);

        ListView lv = (ListView) findViewById(R.id.listView);

        if (savedInstanceState == null || (notes = savedInstanceState.getParcelableArrayList("notes")) == null) {
            notes = new ArrayList<>();
        }
        noteListAdapter = new NoteListAdapter(
                this,
                R.layout.layout_note,
                notes);
        lv.setAdapter(noteListAdapter);
        lv.setEmptyView(findViewById(R.id.empty));
    }

    public void editNote(Note note) {
        Intent intent = new Intent(this, EditActivity.class);
        fillIntentWithNoteInfo(intent, note);
        startActivityForResult(intent, EditActivity.EDIT_NOTE);
    }

    public void deleteNote(final Note note) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete note ?")
                .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        noteListAdapter.remove(note);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case EditActivity.EDIT_NOTE:
                    // -1 : throws OutOfBoundException if there is no such key
                    Note note = notes.get(data.getIntExtra("index", -1));
                    note.setHeader(data.getStringExtra("header"));
                    note.setBody(data.getStringExtra("body"));
                    note.setColor(data.getIntExtra("color", note.getColor()));
                    noteListAdapter.notifyDataSetChanged();
                    return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void addNote(View view) {
        Intent intent = new Intent(this, EditActivity.class);
        int noteIndex = notes.size();
        Note note = new Note("Note " + (noteIndex + 1), "Hello", getResources().getColor(R.color.colorPrimary));
        notes.add(note);
        fillIntentWithNoteInfo(intent, note);
        startActivityForResult(intent, EditActivity.EDIT_NOTE);
    }

    private void fillIntentWithNoteInfo(Intent intent, Note note) {
        intent.putExtra("header", note.getHeader());
        intent.putExtra("body", note.getBody());
        intent.putExtra("color", note.getColor());
        intent.putExtra("index", notes.indexOf(note));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("notes", notes);
    }
}
