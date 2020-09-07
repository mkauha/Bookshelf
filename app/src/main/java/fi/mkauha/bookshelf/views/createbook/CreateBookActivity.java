package fi.mkauha.bookshelf.views.createbook;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomappbar.BottomAppBar;

import java.util.ArrayList;
import java.util.List;

import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.data.local.model.Book;
import fi.mkauha.bookshelf.databinding.ActivityCreateBookBinding;
import fi.mkauha.bookshelf.viewmodel.CreateBookViewModel;
import fi.mkauha.bookshelf.viewmodel.ImageSelectViewModel;
import fi.mkauha.bookshelf.views.modal.ImageSelectModalFragment;

public class CreateBookActivity extends AppCompatActivity {
    private static final String TAG = "CreateBookActivity";

    private ActivityCreateBookBinding binding;
    private BottomAppBar bottomAppBar;
    private Book book;
    private String image;
    private CreateBookViewModel createBookViewModel;
    private ImageSelectViewModel imageSelectViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCreateBookBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);

        createBookViewModel = new ViewModelProvider(this).get(CreateBookViewModel.class);

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

        int uid = getIntent().getIntExtra("BOOK_UID", 0);
        createBookViewModel.findLocalBookById(uid);

        createBookViewModel.getBookEntity().observe(this, book -> {
                Log.d(TAG, "observe: " + book);
                if (book != null) {
                    this.book = book;
                    setBookDataToUI();
                    setTitle(getString(R.string.label_edit_book));
                } else {
                    setTitle(getString(R.string.label_create_book));
                }
        });

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
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.list_item_dropdown, items);
        binding.collection.setAdapter(adapter);

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
                                binding.title.getText().toString(),
                                binding.author.getText().toString(),
                                binding.genre.getText().toString(),
                                binding.year.getText().toString(),
                                binding.pages.getText().toString(),
                                this.image,
                                binding.summary.getText().toString(),
                                binding.language.getText().toString(),
                                binding.collection.getText().toString(),
                                0);
                    } else {
                        book.update(
                                "ISBN",
                                binding.title.getText().toString(),
                                binding.author.getText().toString(),
                                binding.genre.getText().toString(),
                                binding.year.getText().toString(),
                                binding.pages.getText().toString(),
                                this.image,
                                binding.summary.getText().toString(),
                                binding.language.getText().toString(),
                                binding.collection.getText().toString(),
                                0);
                    }

                    createBookViewModel.insertOrUpdate(book);
                    // TODO snackbar
                    finish();
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(R.string.button_cancel, null)
                .show();
    }

    public void setBookDataToUI() {
        //Book book = booksViewModel.getSelected().getValue();
        binding.title.setText(book.getTitle());
        binding.author.setText(book.getAuthor());
        binding.genre.setText(book.getGenres());
        binding.year.setText(book.getYear());
        binding.language.setText(book.getLanguages());
        binding.pages.setText(book.getPages());
        binding.summary.setText(book.getSummary());
        binding.collection.setText(book.getCollection());
        binding.isbn.setText(book.getIsbn());


        this.image = book.getImage();

        Glide.with(this)
                .load(this.image)
                .centerCrop()
                .placeholder(R.drawable.book_cover_placeholder)
                .into(binding.createBookCoverImage);

    }
}