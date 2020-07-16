package fi.mkauha.bookshelf.ui.books;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomappbar.BottomAppBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.databinding.ActivityCreateBookBinding;
import fi.mkauha.bookshelf.models.Book;
import fi.mkauha.bookshelf.ui.modals.ImageSelectModalFragment;
import fi.mkauha.bookshelf.viewmodel.BooksViewModel;
import fi.mkauha.bookshelf.viewmodel.ImageSelectViewModel;

public class CreateBookActivity extends AppCompatActivity {
    private static final String TAG = "CreateBookActivity";

    private ActivityCreateBookBinding binding;
    private BottomAppBar bottomAppBar;
    private Book book;
    private String image;
    private BooksViewModel booksViewModel;
    private ImageSelectViewModel imageSelectViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCreateBookBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);


        bottomAppBar = binding.bottomAppBar;
        bottomAppBar.setNavigationIcon(null);
        bottomAppBar.replaceMenu(R.menu.menu_bottom_create_book);

        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle(getString(R.string.label_create_book));

        Toolbar toolbar =  binding.toolbar;
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(event -> {
            showBackDialog();
        });

        this.book = getIntent().getParcelableExtra("CURRENT_BOOK");
        if(this.book != null) {
            setBookDataToUI();
            setTitle(getString(R.string.label_edit_book));
        } else {
            setTitle(getString(R.string.label_create_book));
        }

        booksViewModel = new ViewModelProvider(this).get(BooksViewModel.class);
        imageSelectViewModel = new ViewModelProvider(this).get(ImageSelectViewModel.class);
        imageSelectViewModel.getSelectedImageFile().observe(this, image -> {
            Log.d(TAG, "selected: " + image);
            this.image = image;
            Glide.with(this)
                    .load(this.image)
                    .centerCrop()
                    .placeholder(R.drawable.book_cover_placeholder)
                    .into(binding.createBookCoverImage);
        });

        binding.createBookCoverImage.setOnClickListener(view -> {
            FragmentManager fragmentManager = this.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            ImageSelectModalFragment fragment = new ImageSelectModalFragment();
            fragmentTransaction.add(fragment, "BottomSheetFragment");
            fragmentTransaction.addToBackStack(null);

            fragmentTransaction.commit();

            // TODO create new navcontroller for this activity
 /*           NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            navController.navigate(R.id.navigation_books);*/
        });

        bottomAppBar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_create_book_ok:
                    showSaveDialog();
            }
            return false;
        });

        // TODO Add possibility to create new collections and save them to persistence
        List<String> items = new ArrayList<>();
        items.add("Toivelista");
        items.add("Opiskelu");
        items.add("Lainatut");
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.dropdown_list_item, items);
        binding.createBookCollection.setAdapter(adapter);

    }

    private void showBackDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.label_go_back)
                .setPositiveButton(R.string.button_ok, (dialog, which) -> {
                    finish();
                })
                .setNegativeButton(R.string.button_cancel, null)
                .show();
    }

    private void showSaveDialog() {

        new AlertDialog.Builder(this)
                .setTitle(R.string.label_save_book)
                .setPositiveButton(R.string.button_ok, (dialog, which) -> {

                    if(book == null) {
                        book = new Book(
                                "ISBN",
                                binding.createBookTitle.getText().toString(),
                                binding.createBookAuthor.getText().toString(),
                                binding.createBookGenre.getText().toString(),
                                binding.createBookYear.getText().toString(),
                                binding.createBookPages.getText().toString(),
                                this.image,
                                binding.createBookSummary.getText().toString(),
                                binding.createBookLanguage.getText().toString(),
                                0);
                    } else {
                        book.update(
                                "ISBN",
                                binding.createBookTitle.getText().toString(),
                                binding.createBookAuthor.getText().toString(),
                                binding.createBookGenre.getText().toString(),
                                binding.createBookYear.getText().toString(),
                                binding.createBookPages.getText().toString(),
                                this.image,
                                binding.createBookSummary.getText().toString(),
                                binding.createBookLanguage.getText().toString(),
                                0);
                    }

                    booksViewModel.insertOrUpdate(book);
                    // TODO snackbar
                    finish();
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(R.string.button_cancel, null)
                .show();
    }

    public void setBookDataToUI() {
        //Book book = booksViewModel.getSelected().getValue();
        binding.createBookTitle.setText(book.getTitle());
        binding.createBookAuthor.setText(book.getAuthor());
        binding.createBookGenre.setText(book.getGenres());
        binding.createBookYear.setText(book.getYear());
        binding.createBookLanguage.setText(book.getLanguages());
        binding.createBookPages.setText(book.getPages());
        binding.createBookSummary.setText(book.getSummary());
        binding.createBookIsbn.setText(book.getIsbn());


        this.image = book.getImage();

        Glide.with(this)
                .load(this.image)
                .centerCrop()
                .placeholder(R.drawable.book_cover_placeholder)
                .into(binding.createBookCoverImage);

    }
}