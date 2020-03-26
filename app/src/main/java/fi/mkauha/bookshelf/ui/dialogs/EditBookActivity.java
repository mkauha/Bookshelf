package fi.mkauha.bookshelf.ui.dialogs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.squareup.picasso.Picasso;

import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.items.BookItem;
import fi.mkauha.bookshelf.ui.books.BooksViewModel;
import fi.mkauha.bookshelf.util.PreferencesUtilities;

import static fi.mkauha.bookshelf.ui.books.BooksFragment.MY_BOOKS_KEY;
import static fi.mkauha.bookshelf.ui.books.BooksFragment.SHARED_PREFS;

public class EditBookActivity extends AppCompatActivity {

    private Action currentAction;

    private boolean editable;

    ImageView etImageView;
    EditText etTitle;
    EditText etAuthor;
    EditText etGenre;
    EditText etImgURL;
    ImageButton editButton;
    Button okButton;
    ColorFilter defaultColorFilter;
    Drawable defaultBackground;

    private String title = "-";
    private String author = "-";
    private String genre = "-";
    private String imgURL = "-";
    private int position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        if(getIntent().getStringExtra("Action").equals("ADD")) {
            setTitle(R.string.add_book);
            currentAction = Action.ADD;
        } else {
            setTitle(R.string.edit_book);
            currentAction = Action.EDIT;
            Intent intent = getIntent();
            title = intent.getStringExtra("Title");
            author = intent.getStringExtra("Author");
            imgURL = intent.getStringExtra("ImgURL");
            position = intent.getIntExtra("Position", 0);
        }

        setContentView(R.layout.activity_edit_book);

        etImageView = findViewById(R.id.editDialog_imageView);
        etTitle = findViewById(R.id.editDialog_bookTitle);
        etAuthor = findViewById(R.id.editDialog_bookAuthor);
        etGenre = findViewById(R.id.editDialog_bookGenre);
        etGenre = findViewById(R.id.editDialog_bookGenre);
        etImgURL = findViewById(R.id.editDialog_bookImageURL);
        editButton = findViewById(R.id.editDialog_editBookButton);
        okButton = findViewById(R.id.editDialog_okButton);

        defaultColorFilter = editButton.getColorFilter();
        defaultBackground = etTitle.getBackground();

        if(currentAction == Action.ADD) {
            enableEditing();
            editButton.setVisibility(View.GONE);
            okButton.setText(R.string.add_book);
        } else {
            disableEditing();
            etTitle.setText(title);
            etAuthor.setText(author);
            etImgURL.setText(imgURL);
            Picasso.get()
                    .load(imgURL)
                    .resize(500, 700)
                    .centerCrop()
                    .placeholder(R.drawable.temp_cover_1)
                    .into(etImageView);
        }

        etImgURL.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                Log.d("etImgURL","done");
                //TODO correct placeholder images
                Picasso.get()
                        .load(v.getText().toString())
                        .resize(500, 700)
                        .centerCrop()
                        .placeholder(R.drawable.temp_cover_1)
                        .into(etImageView);
                handled = true;
            }
            return handled;
        });

    }

    public void onClickOK(View view) {
        // TODO save changes
        addNewBook(view);
/*        if(currentAction == Action.ADD) {
            addNewBook(view);
        } else {
            //updateBook(view) {
        }*/
        finish();
    }
    public void addNewBook(View view) {
        Log.d("EditBookActivity", "addNewBook");
        title = etTitle.getText().toString();
        author = etAuthor.getText().toString();
        genre = etGenre.getText().toString();
        imgURL = etImgURL.getText().toString();

        // TODO unique id generation
        // TODO don't allow empty title and author
        if(imgURL == null || imgURL.equals("")) {
            imgURL = "placeholder";
        }
        BookItem bookItem = new BookItem(1, title, author, genre, imgURL);
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        PreferencesUtilities prefsUtils = new PreferencesUtilities(prefs);
        prefsUtils.putOne(MY_BOOKS_KEY, bookItem);
    }

    public void updateBook() {

    }

    public void onClickEdit(View view) {
        Log.d("EditBookActivity", "onClickEdit");
        if (editable) {
            disableEditing();
        } else {
            enableEditing();
        }
    }

    private void enableEditing() {
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

        etImgURL.setVisibility(View.VISIBLE);

        editable = true;
    }

    private void disableEditing() {
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

        etImgURL.setVisibility(View.GONE);

        editable = false;
    }

}

enum Action {
    EDIT, ADD
}
