package sergeylysyi.threescreens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class EditActivity extends AppCompatActivity {
    public static final int EDIT_NOTE = 1;
    public static final int DEFAULT_COLOR_FOR_INTENT = 0;
    public static final int DEFAULT_INDEX_FOR_INTENT = -1;

    private int index;

    private EditText headerField;
    private EditText bodyField;

    private CurrentColor currentColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        currentColor = new CurrentColor(intent.getIntExtra("color", DEFAULT_COLOR_FOR_INTENT));
        index = intent.getIntExtra("index", DEFAULT_INDEX_FOR_INTENT);

        currentColor.addViewForBackgroundChange(findViewById(R.id.colorView));

        headerField = (EditText) findViewById(R.id.header);
        bodyField = (EditText) findViewById(R.id.body);
        headerField.setText(intent.getStringExtra("header"));
        bodyField.setText(intent.getStringExtra("body"));

        findViewById(R.id.colorView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paletteForResult();
            }
        });
    }

    void paletteForResult() {
        Intent intent = new Intent(this, ScrollPalette.class);
        intent.putExtra("color to edit", currentColor.getColor());
        startActivityForResult(intent, ScrollPalette.REQUEST_PALETTE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finishWithResult();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finishWithResult();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void finishWithResult() {
        Intent result = new Intent();
        result.putExtra("header", headerField.getText().toString());
        result.putExtra("body", bodyField.getText().toString());
        result.putExtra("color", currentColor.getColor());
        result.putExtra("index", index);
        setResult(RESULT_OK, result);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ScrollPalette.REQUEST_PALETTE:
                    currentColor.change(data.getIntExtra("color", 0));
                    return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("current_color", currentColor.getColor());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        int color = savedInstanceState.getInt("current_color");
        currentColor.change(color);
    }
}
