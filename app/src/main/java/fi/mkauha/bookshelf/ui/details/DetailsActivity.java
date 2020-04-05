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
import android.widget.Toast;

import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.squareup.picasso.Picasso;

import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.databinding.ActivityDetailsBinding;
import fi.mkauha.bookshelf.model.BookItem;
import fi.mkauha.bookshelf.util.IDGenerator;
import fi.mkauha.bookshelf.viewmodel.BooksViewModel;
import fi.mkauha.bookshelf.viewmodel.CustomViewModelFactory;

// TODO Refactor data fetching
public class DetailsActivity extends AppCompatActivity {
    private ActivityDetailsBinding binding;

    BooksViewModel booksViewModel;
    Action currentAction;
    ColorFilter defaultColorFilter;
    Drawable defaultBackground;
    BookItem bookItemInEdit;

    private boolean editable = true;
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
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);

        booksViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getApplication())).get(BooksViewModel.class);
        Intent intent = getIntent();

        if(intent.getStringExtra("Action").equals("ADD")) {
            currentAction = Action.ADD;
            prefsKey = intent.getStringExtra("ViewModel_Key");
        } else {
            currentAction = Action.VIEW;
            prefsKey = intent.getStringExtra("ViewModel_Key");
            id = intent.getIntExtra("ID", -1);
            position = intent.getIntExtra("Position", 0);
        }

        defaultColorFilter = binding.detailsEdit.getColorFilter();
        defaultBackground = binding.detailsBookTitle.getBackground();

        if(prefsKey.equals(BooksViewModel.WISHLIST_BOOKS_KEY)) {
            binding.detailsBookmark.setVisibility(View.GONE);
            binding.detailsAddAsOwnedButton.setVisibility(View.VISIBLE);
        } else {
            binding.detailsBookmark.setVisibility(View.VISIBLE);
            binding.detailsAddAsOwnedButton.setVisibility(View.GONE);
        }

        if(currentAction == Action.ADD) {
            setEditingMode(true);
            binding.detailsEdit.setVisibility(View.GONE);
            binding.detailsOkButton.setText(R.string.add_book);
            binding.detailsAddAsOwnedButton.setVisibility(View.GONE);

        } else if(currentAction == Action.VIEW) {
            setEditingMode(false);
            bookItemInEdit = booksViewModel.getOne(prefsKey, this.id);
            binding.detailsBookTitle.setText(bookItemInEdit.getTitle());
            binding.detailsBookAuthor.setText(bookItemInEdit.getAuthor());
            binding.detailsBookGenre.setText(bookItemInEdit.getGenre());
            binding.detailsBookImageURL.setText(bookItemInEdit.getImgURL());
            binding.detailsBookmark.setText(bookItemInEdit.getBookmark());
            Picasso.get()
                    .load(bookItemInEdit.getImgURL())
                    .resize(500, 700)
                    .centerCrop()
                    .placeholder(R.drawable.book_cover_placeholder)
                    .into(binding.detailsImageView);
        }

        binding.detailsBookImageURL.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                Log.d("etImgURL","done");
                //TODO correct placeholder images
                Picasso.get()
                        .load(v.getText().toString())
                        .resize(500, 700)
                        .centerCrop()
                        .placeholder(R.drawable.book_cover_placeholder)
                        .into(binding.detailsImageView);
                handled = true;
            }
            return handled;
        });
    }

    public void onClickOK(View view) {
        title = binding.detailsBookTitle.getText().toString();
        author = binding.detailsBookAuthor.getText().toString();
        genre = binding.detailsBookGenre.getText().toString();
        imgURL = binding.detailsBookImageURL.getText().toString();

        // TODO don't allow empty title and author
        if(imgURL.equals("")) {
            imgURL = "R.drawable.book_cover_placeholder";
        }

        if(currentAction == Action.ADD) {
            addNewBook(prefsKey);
        } else {
            bookmark = binding.detailsBookmark.getText().toString();
            updateBook(prefsKey);
        }
        finish();
    }
    public void addNewBook(String prefsKey) {
        BookItem bookItem = new BookItem(IDGenerator.generate(), title, author, genre, imgURL);
        booksViewModel.putOne(prefsKey, bookItem);
    }

    public void updateBook(String prefsKey) {
        bookItemInEdit.setTitle(title);
        bookItemInEdit.setAuthor(author);
        bookItemInEdit.setGenre(genre);
        bookItemInEdit.setImgURL(imgURL);
        bookItemInEdit.setBookmark(bookmark);
        booksViewModel.updateOne(prefsKey, bookItemInEdit);
    }

    public void onClickEdit(View view) {
        if(!editable) {
            setEditingMode(true);
        } else {
            setEditingMode(false);
        }
    }

    public void onClickRemove(View view) {
        booksViewModel.removeOne(prefsKey, this.id);
        finish();
    }

    public void onClickAddAsOwned(View view) {
        id = bookItemInEdit.getBookID();
        title = bookItemInEdit.getTitle();
        author = bookItemInEdit.getAuthor();
        genre = bookItemInEdit.getGenre();
        imgURL = bookItemInEdit.getImgURL();
        addNewBook(BooksViewModel.MY_BOOKS_KEY);
        booksViewModel.removeOne(prefsKey, this.id);
        Toast.makeText(this,R.string.added_as_owned,Toast.LENGTH_LONG).show();
        binding.detailsBookmark.setVisibility(View.VISIBLE);
        binding.detailsAddAsOwnedButton.setVisibility(View.GONE);
        finish();
    }

    private void setEditingMode(boolean editingMode) {
        if(editingMode) {
            editable = true;
            binding.detailsBookImageURL.setVisibility(View.VISIBLE);
            binding.detailsRemove.setVisibility(View.VISIBLE);

            binding.detailsEdit.setColorFilter(Color.RED);
            binding.detailsBookTitle.setBackground(defaultBackground);
            binding.detailsBookAuthor.setBackground(defaultBackground);
            binding.detailsBookGenre.setBackground(defaultBackground);
            binding.detailsBookmark.setBackground(defaultBackground);
        } else {
            editable = false;
            binding.detailsBookImageURL.setVisibility(View.GONE);
            binding.detailsRemove.setVisibility(View.GONE);

            binding.detailsEdit.setColorFilter(defaultColorFilter);
            binding.detailsBookTitle.setBackground(null);
            binding.detailsBookAuthor.setBackground(null);
            binding.detailsBookGenre.setBackground(null);
            binding.detailsBookmark.setBackground(null);
        }

        binding.detailsBookTitle.setFocusableInTouchMode(editingMode);
        binding.detailsBookTitle.setFocusable(editingMode);
        binding.detailsBookTitle.setEnabled(editingMode);

        binding.detailsBookAuthor.setFocusableInTouchMode(editingMode);
        binding.detailsBookAuthor.setFocusable(editingMode);
        binding.detailsBookAuthor.setEnabled(editingMode);

        binding.detailsBookGenre.setFocusableInTouchMode(editingMode);
        binding.detailsBookGenre.setFocusable(editingMode);
        binding.detailsBookGenre.setEnabled(editingMode);

        binding.detailsBookmark.setFocusableInTouchMode(editingMode);
        binding.detailsBookmark.setFocusable(editingMode);
        binding.detailsBookmark.setEnabled(editingMode);
    }
}

enum Action {
    VIEW, ADD
}
