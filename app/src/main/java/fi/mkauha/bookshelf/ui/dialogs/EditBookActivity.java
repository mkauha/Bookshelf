package fi.mkauha.bookshelf.ui.dialogs;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import fi.mkauha.bookshelf.R;



public class EditBookActivity extends AppCompatActivity {

    private boolean editable;

    EditText etTitle;
    EditText etAuthor;
    EditText etGenre;
    ImageButton editButton;
    ColorFilter defaultColorFilter;
    Drawable defaultBackground;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setTitle(R.string.edit_book);
        setContentView(R.layout.activity_edit_book);

        etTitle = findViewById(R.id.editDialog_bookTitle);
        etAuthor = findViewById(R.id.editDialog_bookAuthor);
        etGenre = findViewById(R.id.editDialog_bookGenre);
        editButton = findViewById(R.id.editDialog_editBookButton);
        editable = false;
        defaultColorFilter = editButton.getColorFilter();
        defaultBackground = etTitle.getBackground();
        etTitle.setBackground(null);
        etAuthor.setBackground(null);
        etGenre.setBackground(null);
    }

    public void onClickOK(View view) {
        // TODO save changes
        finish();
    }

    public void onClickEdit(View view) {
        Log.d("onClickEdit", "clicked");
        if(editable) {
            disableEditText();
        } else {
            enableEditText();
        }
    }
    private void enableEditText() {
        editButton.setColorFilter(Color.RED);
        etTitle.setFocusableInTouchMode(true);
        etTitle.setFocusable(true);
        etTitle.setEnabled(true);
        etTitle.setBackground(defaultBackground);

        etAuthor.setFocusableInTouchMode(true);
        etAuthor.setFocusable(true);
        etAuthor.setEnabled(true);
        etAuthor.setBackground(defaultBackground);

        etGenre.setFocusableInTouchMode(true);
        etGenre.setFocusable(true);
        etGenre.setEnabled(true);
        etGenre.setBackground(defaultBackground);

        editable = true;
    }

    private void disableEditText() {
        editButton.setColorFilter(defaultColorFilter);
        etTitle.setFocusableInTouchMode(false);
        etTitle.setFocusable(false);
        etTitle.setEnabled(false);
        etTitle.setBackground(null);

        etAuthor.setFocusableInTouchMode(false);
        etAuthor.setFocusable(false);
        etAuthor.setEnabled(false);
        etAuthor.setBackground(null);

        etGenre.setFocusableInTouchMode(false);
        etGenre.setFocusable(false);
        etGenre.setEnabled(false);
        etGenre.setBackground(null);
        editable = false;
    }
}
