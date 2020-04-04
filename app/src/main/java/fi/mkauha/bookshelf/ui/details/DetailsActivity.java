package fi.mkauha.bookshelf.ui.details;

import android.content.Intent;
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
import android.widget.Toast;

import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.squareup.picasso.Picasso;

import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.model.BookItem;
import fi.mkauha.bookshelf.util.IDGenerator;
import fi.mkauha.bookshelf.viewmodel.BooksViewModel;
import fi.mkauha.bookshelf.viewmodel.CustomViewModelFactory;

public class DetailsActivity extends AppCompatActivity {
    BooksViewModel booksViewModel;
    private Action currentAction;

    private boolean editable;

    ImageView etImageView;
    EditText etTitle;
    EditText etAuthor;
    EditText etGenre;
    EditText etImgURL;
    EditText etBookmark;
    ImageButton editButton;
    ImageButton addAsOwnedButton;
    ImageButton removeBookButton;
    Button okButton;
    ColorFilter defaultColorFilter;
    Drawable defaultBackground;

    BookItem bookItemInEdit;

    private int id;
    private String prefsKey;
    private String bookmark;
    private String title = "-";
    private String author = "-";
    private String genre = "-";
    private String imgURL = "-";
    private int position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        booksViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getApplication())).get(BooksViewModel.class);
        Intent intent = getIntent();

        if(intent.getStringExtra("Action").equals("ADD")) {
            setTitle(R.string.add_book);
            currentAction = Action.ADD;
            prefsKey = intent.getStringExtra("ViewModel_Key");
            Log.d("DetailsActivity","key :" + prefsKey);
        } else {
            setTitle(R.string.edit_book);
            currentAction = Action.EDIT;
            prefsKey = intent.getStringExtra("ViewModel_Key");

            id = intent.getIntExtra("ID", -1);
            Log.d("DetailsActivity","ID :" + id);
            position = intent.getIntExtra("Position", 0);
        }

        setContentView(R.layout.activity_details);

        etImageView = findViewById(R.id.editDialog_imageView);
        etTitle = findViewById(R.id.editDialog_bookTitle);
        etAuthor = findViewById(R.id.editDialog_bookAuthor);
        etGenre = findViewById(R.id.editDialog_bookGenre);
        etGenre = findViewById(R.id.editDialog_bookGenre);
        etImgURL = findViewById(R.id.editDialog_bookImageURL);
        etBookmark = findViewById(R.id.editDialog_bookmark);
        addAsOwnedButton = findViewById(R.id.editDialog_mark_owned);
        removeBookButton = findViewById(R.id.editDialog_removeBookButton);
        editButton = findViewById(R.id.editDialog_editBookButton);
        okButton = findViewById(R.id.editDialog_okButton);

        defaultColorFilter = editButton.getColorFilter();
        defaultBackground = etTitle.getBackground();

        if(prefsKey.equals(BooksViewModel.WISHLIST_BOOKS_KEY)) {
            etBookmark.setVisibility(View.GONE);
            addAsOwnedButton.setVisibility(View.VISIBLE);
        } else {
            etBookmark.setVisibility(View.VISIBLE);
            addAsOwnedButton.setVisibility(View.GONE);
        }

        if(currentAction == Action.ADD) {
            enableEditing();
            editButton.setVisibility(View.GONE);
            okButton.setText(R.string.add_book);
            if(prefsKey.equals(BooksViewModel.WISHLIST_BOOKS_KEY)) {
                addAsOwnedButton.setVisibility(View.GONE);
            }
        } else {
            disableEditing();
            Log.d("DetailsActivity","key :" + prefsKey);
            bookItemInEdit = booksViewModel.getOne(prefsKey, this.id);
            etTitle.setText(bookItemInEdit.getTitle());
            etAuthor.setText(bookItemInEdit.getAuthor());
            etGenre.setText(bookItemInEdit.getGenre());
            etImgURL.setText(bookItemInEdit.getImgURL());
            etBookmark.setText(bookItemInEdit.getBookmark());
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
        if(currentAction == Action.ADD) {
            addNewBook(prefsKey);
        } else {
            updateBook(prefsKey);
        }
        finish();
    }
    public void addNewBook(String prefsKey) {
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
        booksViewModel.putOne(prefsKey, bookItem);
    }

    public void updateBook(String prefsKey) {
        Log.d("EditBookActivity", "updateBook");
        title = etTitle.getText().toString();
        author = etAuthor.getText().toString();
        genre = etGenre.getText().toString();
        imgURL = etImgURL.getText().toString();
        bookmark = etBookmark.getText().toString();

        // TODO don't allow empty title and author
        if(imgURL == null || imgURL.equals("")) {
            imgURL = "";
        }
        bookItemInEdit.setTitle(title);
        bookItemInEdit.setAuthor(author);
        bookItemInEdit.setGenre(genre);
        bookItemInEdit.setImgURL(imgURL);
        bookItemInEdit.setBookmark(bookmark);

        boolean updated = booksViewModel.updateOne(prefsKey, bookItemInEdit);
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

    public void onClickRemove(View view) {
        Log.d("EditBookActivity", "onClickRemove");
        booksViewModel.removeOne(prefsKey, this.id);
        finish();
    }

    public void onClickAddAsOwned(View view) {
        addNewBook(BooksViewModel.MY_BOOKS_KEY);
        booksViewModel.removeOne(prefsKey, this.id);
        Toast.makeText(this,R.string.added_as_owned,Toast.LENGTH_LONG).show();
        etBookmark.setVisibility(View.VISIBLE);
        addAsOwnedButton.setVisibility(View.GONE);
        finish();
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

        etBookmark.setFocusableInTouchMode(true);
        etBookmark.setFocusable(true);
        etBookmark.setEnabled(true);
        etBookmark.setBackground(defaultBackground);

        etImgURL.setVisibility(View.VISIBLE);
        removeBookButton.setVisibility(View.VISIBLE);

        editable = true;
    }

    private void disableEditing() {
        editButton.setColorFilter(defaultColorFilter);
        etTitle.setFocusableInTouchMode(false);
        etTitle.setFocusable(false);
        //etTitle.setEnabled(false);
        etTitle.setBackground(null);

        etAuthor.setFocusableInTouchMode(false);
        etAuthor.setFocusable(false);
        //etAuthor.setEnabled(false);
        etAuthor.setBackground(null);

        etGenre.setFocusableInTouchMode(false);
        etGenre.setFocusable(false);
        //etGenre.setEnabled(false);
        etGenre.setBackground(null);

        etImgURL.setVisibility(View.GONE);
        removeBookButton.setVisibility(View.GONE);

        etBookmark.setFocusableInTouchMode(false);
        etBookmark.setFocusable(false);
        //etBookmark.setEnabled(false);
        etBookmark.setBackground(null);

        editable = false;
    }

}

enum Action {
    EDIT, ADD
}
