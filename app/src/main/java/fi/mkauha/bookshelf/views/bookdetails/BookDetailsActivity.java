package fi.mkauha.bookshelf.views.bookdetails;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomappbar.BottomAppBar;

import java.util.ArrayList;
import java.util.List;

import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.data.local.model.Book;
import fi.mkauha.bookshelf.data.remote.model.Record;
import fi.mkauha.bookshelf.databinding.ActivityBookDetailsBinding;
import fi.mkauha.bookshelf.viewmodel.BookDetailsViewModel;
import fi.mkauha.bookshelf.views.createbook.CreateBookActivity;

public class BookDetailsActivity extends AppCompatActivity {
    private static final String TAG = "BookDetailsActivity";

    private ActivityBookDetailsBinding binding;
    private BookDetailsViewModel bookDetailsViewModel;
    private Book book;
    private BottomAppBar bottomAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityBookDetailsBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);

        bookDetailsViewModel = new ViewModelProvider(this).get(BookDetailsViewModel.class);
        bookDetailsViewModel.init();

        bottomAppBar = binding.bottomAppBar;
        bottomAppBar.setNavigationIcon(null);
        bottomAppBar.replaceMenu(R.menu.menu_bottom_book_details);
        bottomAppBar.setVisibility(View.GONE);

        if(getIntent().hasExtra("BOOK_UID")) {
            int uid = getIntent().getIntExtra("BOOK_UID", 0);
            Log.d(TAG, "uid: " + uid);
            bookDetailsViewModel.findLocalBookById(uid);

            bookDetailsViewModel.getBookEntity().observe(this, book -> {
                Log.d(TAG, "observe: " + book);
                if(null != book) {
                    // binding.loadingProgress.setVisibility(View.GONE);
                this.book = book;
                Glide.with(this)
                        .load(book.getImage())
                        .centerCrop()
                        .placeholder(R.drawable.book_cover_placeholder)
                        .into(binding.bookDetailsCoverImage);

                    binding.title.setText(book.getTitle());
                    binding.author.setText(book.getAuthor());
                    binding.genre.setText(book.getGenres());
                    binding.language.setText(book.getLanguages());
                    binding.year.setText(book.getYear());
                    binding.pages.setText(String.valueOf(book.getPages()));
                    binding.summary.setText(book.getSummary());
                    binding.collection.setText(book.getCollection());
                    binding.isbn.setText(book.getIsbn());

                    Toolbar toolbar = binding.toolbar;
                    setSupportActionBar(toolbar);
                    toolbar.setNavigationOnClickListener(event -> {
                        finish();
                    });
                    CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
                    toolBarLayout.setExpandedTitleColor(getResources().getColor(R.color.colorPrimaryTextLight));

                    if(book.getTitle() == null || book.getTitle().equals("")) {
                        setTitle(" ");
                    } else {
                        setTitle(book.getTitle());
                    }

                }
            });
        } else if(getIntent().hasExtra("RECORD_ID")) {
            String id = getIntent().getStringExtra("RECORD_ID");
            Log.d(TAG, "ID: " + id);
            bookDetailsViewModel.searchRecordById(id);
            bookDetailsViewModel.getSearchResults().observe(this,
                    record -> {
                        Log.d(TAG, "mAdapter.setBooks " + record);
                        setBookDataToUi(record);
                    }
            );

            Toolbar toolbar = binding.toolbar;
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(event -> {
                finish();
            });
            CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
            toolBarLayout.setExpandedTitleColor(getResources().getColor(R.color.colorPrimaryTextLight));


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
                    .setTitle(R.string.label_edit_book_dialog)
                    .setPositiveButton(R.string.button_ok, (dialog, which) -> {

                        if(this.book != null) {
                            Intent intent = new Intent(this, CreateBookActivity.class);
                            intent.putExtra("BOOK_UID", this.book.getUid());
                            this.startActivity(intent);
                            finish();
                        }

                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(R.string.button_cancel, null)
                    .show();
        });

        binding.delete.setOnClickListener(view -> {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.label_delete_book_dialog)
                    .setPositiveButton(R.string.button_ok, (dialog, which) -> {
                        bookDetailsViewModel.deleteLocalBook(this.book);
                        finish();
                        // TODO snackbar
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(R.string.button_cancel, null)
                    .show();
        });

        // TODO Add possibility to create new collections and save them to persistence
        List<String> items = new ArrayList<>();
        items.add("Toivelista");
        items.add("Opiskelu");
        items.add("Lainatut");
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.list_item_dropdown, items);
        binding.collection.setAdapter(adapter);

    }

    private void setBookDataToUi(Record record) {
        Log.d(TAG, "observe: " + record);
        if(null != record) {
            // binding.loadingProgress.setVisibility(View.GONE);
            String imageURL = "https://sociology.indiana.edu/images/publications/book-cover-placeholder.jpg";
            if (record.getImages().size() > 0) {
                imageURL = "https://api.finna.fi" + record.getImages().get(0);
            }

            Glide.with(this)
                    .load(imageURL)
                    .centerCrop()
                    .placeholder(R.drawable.book_cover_placeholder)
                    .into(binding.bookDetailsCoverImage);

            binding.title.setText(record.getTitle());
            binding.author.setText(record.getNonPresenterAuthors().get(0).getName());
            binding.genre.setText(record.getGenres().get(0));
            binding.language.setText(record.getLanguages().get(0));
            binding.year.setText(record.getYear());
            binding.pages.setText(record.getPhysicalDescriptions().get(0));
            binding.summary.setText(record.getSummary().get(0));
            binding.collection.setVisibility(View.GONE);
            binding.isbn.setText(record.getCleanIsbn());

            if(record.getTitle() == null || record.getTitle().equals("")) {
                setTitle(" ");
            } else {
                setTitle(record.getTitle());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bottom_book_details, menu);
        return true;
    }
}