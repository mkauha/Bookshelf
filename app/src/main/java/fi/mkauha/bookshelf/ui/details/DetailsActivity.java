package fi.mkauha.bookshelf.ui.details;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import android.view.Gravity;
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
import fi.mkauha.bookshelf.models.BookItem;
import fi.mkauha.bookshelf.util.IDGenerator;
import fi.mkauha.bookshelf.viewmodel.BooksViewModel;
import fi.mkauha.bookshelf.viewmodel.CustomViewModelFactory;

/**
 * Activity that displays book item data and allows editing existing and creation of new book items.
 *
 * Shows book image, title, author, genre.
 * In "View" mode has button to enable editing of book data.
 * In "Add" mode initializes with empty fields that can be filled with book data and added to either "My books" or "Wish list".
*  If activity is opened from "My books" shows bookmark. If opened from "Wish list" shows button that moves book to "My books"
 *
 * @author  Miko Kauhanen
 * @version 1.0
 */
public class DetailsActivity extends AppCompatActivity {

    /**
     * Activity binding.
     */
    private ActivityDetailsBinding binding;

    /**
     * The Books view model.
     */
    BooksViewModel booksViewModel;
    /**
     * The Current action. Either "Add" or "View".
     */
    Action currentAction;
    /**
     * The Default color filter for text fields.
     */
    ColorFilter defaultColorFilter;
    /**
     * The Default background for text fields.
     */
    Drawable defaultBackground;
    /**
     * The Book item in edit.
     */
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

    /**
     * Initializes fields, receives intent and modifies the layout based on given mode.
     *
     * If mode is "Add" all TextFields are empty, edit and setAsOwned button are hidden.
     * If mode is "View" all TextFields are filled and cover image is fetched with book data that is retrieved from ViewModel with book ID.
     * Also shows buttons for editing and setting as owned. Sets listener to image URL TextField that fetches image when finished typing for previewing.
     *
     * @param savedInstanceState the savedInstanceState
     */
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

    /**
     * On ok-button click gets book data and depending on mode updates book item data or adds a new book to viewModel.
     *
     * @param view the view
     */
    public void onClickOK(View view) {
        title = binding.detailsBookTitle.getText().toString();
        author = binding.detailsBookAuthor.getText().toString();
        genre = binding.detailsBookGenre.getText().toString();
        imgURL = binding.detailsBookImageURL.getText().toString();

        // TODO don't allow empty title and author
        if(imgURL.equals("")) {
            imgURL = "No image URL";
        }

        if(currentAction == Action.ADD) {
            addNewBook(prefsKey, -1);
        } else {
            bookmark = binding.detailsBookmark.getText().toString();
            updateBook(prefsKey);
        }
        finish();
    }

    /**
     * Adds new book to ViewModel.
     *
     * Generates new id and creates new BookItem with data from TextFields. Puts this BookItem to ViewModel.
     *
     * @param prefsKey the SharedPreferences key
     * @param id       the book item id
     */
    public void addNewBook(String prefsKey, int id) {
        // TODO fix multiadd
        if(id == -1) {
            id = IDGenerator.generate(getApplicationContext());
        }

        BookItem bookItem = new BookItem(id, title, author, genre, imgURL);
        booksViewModel.putOne(prefsKey, bookItem);
    }

    /**
     * Updates current book data to ViewModel.
     *
     * @param prefsKey the SharedPreferences key
     */
    public void updateBook(String prefsKey) {
        bookItemInEdit.setTitle(title);
        bookItemInEdit.setAuthor(author);
        bookItemInEdit.setGenre(genre);
        bookItemInEdit.setImgURL(imgURL);
        bookItemInEdit.setBookmark(bookmark);
        booksViewModel.updateOne(prefsKey, bookItemInEdit);
    }

    /**
     * Enables editing mode.
     *
     * Allows modification of TextFields and removing book.
     *
     * @param view the view
     */
    public void onClickEdit(View view) {
        if(!editable) {
            setEditingMode(true);
        } else {
            setEditingMode(false);
        }
    }

    /**
     * Removes book from ViewModel.
     *
     * @param view the view
     */
    public void onClickRemove(View view) {
        booksViewModel.removeOne(prefsKey, this.id);
        finish();
    }

    /**
     * Open confirm dialog to move book to owned books.
     *
     * @param view the view
     */
    public void onClickAddAsOwned(View view) {
        openConfirmDialog();
    }

    /**
     * Dialog for moving book to owned books.
     *
     * On positive button click moves book from WishList to MyBooks.
     * Changes Activity layout to match changes in location and displays a toast message.
     */
    public void openConfirmDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(getResources().getString(R.string.move_to_owned_confirm));
        alertDialogBuilder.setPositiveButton(getResources().getString(R.string.button_yes),
                (arg0, arg1) -> {
                    id = bookItemInEdit.getBookID();
                    title = bookItemInEdit.getTitle();
                    author = bookItemInEdit.getAuthor();
                    genre = bookItemInEdit.getGenre();
                    imgURL = bookItemInEdit.getImgURL();
                    addNewBook(BooksViewModel.MY_BOOKS_KEY, id);
                    booksViewModel.removeOne(prefsKey, getId());

                    Toast toast = Toast.makeText(getApplicationContext(),R.string.move_to_owned,Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();

                    binding.detailsBookmark.setVisibility(View.VISIBLE);
                    binding.detailsAddAsOwnedButton.setVisibility(View.GONE);

                    finish();
                });

        alertDialogBuilder.setNegativeButton(getResources().getString(R.string.button_cancel), (dialog, which) -> {

        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /**
     * Changes editing mode.
     *
     * When editing is enabled all TextFields can be modified and image URL is shown.
     *
     * @param editingMode is editing enabled
     */
    private void setEditingMode(boolean editingMode) {
        if(editingMode) {
            editable = true;
            binding.detailsBookImageURL.setVisibility(View.VISIBLE);
            binding.detailsRemove.setVisibility(View.VISIBLE);

            binding.detailsEdit.setColorFilter(Color.RED);
            binding.detailsBookTitle.setBackground(defaultBackground);
            binding.detailsBookAuthor.setBackground(defaultBackground);
            binding.detailsBookGenre.setBackground(defaultBackground);
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

        binding.detailsBookAuthor.setFocusableInTouchMode(editingMode);
        binding.detailsBookAuthor.setFocusable(editingMode);

        binding.detailsBookGenre.setFocusableInTouchMode(editingMode);
        binding.detailsBookGenre.setFocusable(editingMode);
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }
}

/**
 * The enum Action.
 */
enum Action {
    VIEW, ADD
}
