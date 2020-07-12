package fi.mkauha.bookshelf.ui.books;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.databinding.ActivityBookDetailsBinding;
import fi.mkauha.bookshelf.databinding.FragmentBookDetailsBinding;
import fi.mkauha.bookshelf.models.Book;
import fi.mkauha.bookshelf.viewmodel.BooksViewModel;

public class BookDetailsActivity extends AppCompatActivity {
    private static final String TAG = "BookDetailsActivity";

    private ActivityBookDetailsBinding binding;
    private BooksViewModel booksViewModel;
    private Book book;
    private BottomAppBar bottomAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityBookDetailsBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);

        booksViewModel = new ViewModelProvider(this).get(BooksViewModel.class);
        bottomAppBar = binding.bottomAppBar;
        bottomAppBar.setNavigationIcon(null);
        bottomAppBar.replaceMenu(R.menu.menu_bottom_book_details);

        this.book = getIntent().getParcelableExtra("CURRENT_BOOK");

        Glide.with(this)
                .load(book.getImage())
                .centerCrop()
                .placeholder(R.drawable.book_cover_placeholder)
                .into(binding.bookDetailsCoverImage);

        //binding.bookDetailsBookTitle.setText(book.getTitle());
        binding.bookDetailsBookAuthors.setText(book.getAuthor());
        binding.bookDetailsBookYear.setText(book.getYear());
        binding.bookDetailsBookLanguage.setText(book.getLanguages());
        binding.bookDetailsBookPages.setText(String.valueOf(book.getPages()));

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(event -> {
            finish();
        });
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        Log.d(TAG, "Title: " + book.getTitle());
        if(book.getTitle() == null || book.getTitle().equals("")) {
            toolBarLayout.setTitle(" ");
        } else {
            toolBarLayout.setTitle(book.getTitle());
        }


        bottomAppBar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.book_details_share:
                    Log.d(TAG, "Share");
                    break;
                case R.id.book_details_favorite:
                    Log.d(TAG, "Favorite");
                    break;
                case R.id.book_details_bookmark:
                    Log.d(TAG, "Bookmark");
                    break;
            }
            return false;
        });

        binding.edit.setOnClickListener(view -> {
            new AlertDialog.Builder(this)
                    .setTitle("Edit book?")
                    .setPositiveButton(R.string.button_ok, (dialog, which) -> {
                        Intent intent = new Intent(this, CreateBookActivity.class);
                        intent.putExtra("CURRENT_BOOK", (Parcelable) this.book);
                        this.startActivity(intent);
                        finish();
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(R.string.button_cancel, null)
                    .show();
        });

        binding.delete.setOnClickListener(view -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete book?")
                    .setPositiveButton(R.string.button_ok, (dialog, which) -> {
                        booksViewModel.delete(this.book);
                        finish();
                        // TODO snackbar
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(R.string.button_cancel, null)
                    .show();
        });

    }
}