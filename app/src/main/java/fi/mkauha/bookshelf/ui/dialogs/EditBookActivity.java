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

import com.squareup.picasso.Picasso;

import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.items.BookItem;
import fi.mkauha.bookshelf.util.IDGenerator;
import fi.mkauha.bookshelf.util.PreferencesUtilities;

import static fi.mkauha.bookshelf.ui.books.BooksFragment.MY_BOOKS_KEY;
import static fi.mkauha.bookshelf.ui.books.BooksFragment.SHARED_PREFS;

public class EditBookActivity extends AppCompatActivity {

    SharedPreferences prefs;
    PreferencesUtilities prefsUtils;

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

    BookItem bookItemInEdit;

    private int id;
    private String title = "-";
    private String author = "-";
    private String genre = "-";
    private String imgURL = "-";
    private int position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        prefs = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        prefsUtils = new PreferencesUtilities(prefs);

        if(getIntent().getStringExtra("Action").equals("ADD")) {
            setTitle(R.string.add_book);
            currentAction = Action.ADD;
        } else {
            setTitle(R.string.edit_book);
            currentAction = Action.EDIT;
            Intent intent = getIntent();
            id = intent.getIntExtra("ID", -1);
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
            bookItemInEdit = prefsUtils.getOne(MY_BOOKS_KEY, this.id);
            etTitle.setText(bookItemInEdit.getTitle());
            etAuthor.setText(bookItemInEdit.getAuthor());
            etGenre.setText(bookItemInEdit.getGenre());
            etImgURL.setText(bookItemInEdit.getImgURL());
            Picasso.get()
                    .load(bookItemInEdit.getImgURL())
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
        //addNewBook(view);
        if(currentAction == Action.ADD) {
            addNewBook();
        } else {
            updateBook();
        }
        finish();
    }
    public void addNewBook() {
        Log.d("EditBookActivity", "addNewBook");
        title = etTitle.getText().toString();
        author = etAuthor.getText().toString();
        genre = etGenre.getText().toString();
        imgURL = etImgURL.getText().toString();

        // TODO don't allow empty title and author
        if(imgURL == null || imgURL.equals("")) {
            imgURL = "placeholder";
        }
        BookItem bookItem = new BookItem(IDGenerator.generate(), title, author, genre, imgURL);
        prefsUtils.putOne(MY_BOOKS_KEY, bookItem);
    }

    public void updateBook() {
        Log.d("EditBookActivity", "updateBook");
        title = etTitle.getText().toString();
        author = etAuthor.getText().toString();
        genre = etGenre.getText().toString();
        imgURL = etImgURL.getText().toString();

        // TODO don't allow empty title and author
        if(imgURL == null || imgURL.equals("")) {
            imgURL = "placeholder";
        }
        bookItemInEdit.setTitle(title);
        bookItemInEdit.setAuthor(author);
        bookItemInEdit.setGenre(genre);
        bookItemInEdit.setImgURL(imgURL);

        boolean updated = prefsUtils.updateOne(MY_BOOKS_KEY, bookItemInEdit);
        Log.d("EditBookActivity", "updateBook: " + updated);
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
