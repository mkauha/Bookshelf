package fi.mkauha.bookshelf.ui.books;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.databinding.FragmentManualAddBookBinding;
import fi.mkauha.bookshelf.models.Book;
import fi.mkauha.bookshelf.viewmodel.BooksViewModel;



public class ManualAddBookFragment extends Fragment {

    private FragmentManualAddBookBinding binding;
    private BooksViewModel booksViewModel;
    private BottomAppBar bottomAppBar;
    private FloatingActionButton fab;
    private Book book;
    private String image;
    public static final int PICK_IMAGE = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentManualAddBookBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        setHasOptionsMenu(true);

        bottomAppBar = (BottomAppBar) getActivity().findViewById(R.id.bottom_app_bar);
        bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);

        bottomAppBar.setNavigationIcon(null);
        bottomAppBar.performShow();

        fab = getActivity().findViewById(R.id.fab);
        fab.setImageDrawable(getActivity().getDrawable(R.drawable.ic_outline_save_24));
        fab.show();

        fab.setOnClickListener(view -> {
            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.navigation_books);
        });

        bottomAppBar.replaceMenu(R.menu.bottom_manual_add_book_menu);
        booksViewModel = new ViewModelProvider(getActivity()).get(BooksViewModel.class);
        setBookDataToUI();



        ViewGroup.LayoutParams defaultParams = binding.fragmentManualAddBook.getLayoutParams();

        // TODO Add string values
        bottomAppBar.setOnMenuItemClickListener(item -> {
            Log.d("ManualAddBookFragment", "Cancel");
            switch (item.getItemId()) {
                case R.id.navigation_books:
                    new AlertDialog.Builder(getContext())
                            .setTitle("Stop editing?")
                            .setMessage("Book data will be lost")
                            .setPositiveButton("Stop", (dialog, which) -> {
                                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                                NavigationUI.onNavDestinationSelected(item, navController);
                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton("Continue editing", null)
                            .show();
                    break;
            }
            return false;
        });

        fab.setOnClickListener(view -> {
            new AlertDialog.Builder(getContext())
                    .setTitle("Save book?")
                    .setPositiveButton("Save", (dialog, which) -> {
                        book = new Book(
                                "ISBN",
                                binding.manualAddBookTitle.getText().toString(),
                                binding.manualAddBookAuthors.getText().toString(),
                                binding.manualAddBookGenres.getText().toString(),
                                binding.manualAddBookYear.getText().toString(),
                                binding.manualAddBookPages.getText().toString(),
                                this.image,
                                binding.manualAddBookSummary.getText().toString(),
                                binding.manualAddBookLanguage.getText().toString(),
                                0);
                        booksViewModel.insert(book);
                        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                        navController.navigate(R.id.navigation_books);
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton("Continue editing", null)
                    .show();
        });

        KeyboardVisibilityEvent.setEventListener(
                getActivity(),
                isOpen -> {
                    if(isOpen) {
                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                                FrameLayout.LayoutParams.MATCH_PARENT,
                                FrameLayout.LayoutParams.MATCH_PARENT
                        );
                        params.setMargins(0, 0, 0, 55);
                        binding.fragmentManualAddBook.setLayoutParams(params);

                        bottomAppBar.performHide();
                        fab.hide();
                    } else {
                        binding.fragmentManualAddBook.setLayoutParams(defaultParams);
                        bottomAppBar.performShow();
                        fab.show();
                    }
                });

        binding.manualAddBookCoverImage.setOnClickListener(view -> {
            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
            getIntent.setType("image/*");

            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setType("image/*");

            Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

            startActivityForResult(chooserIntent, PICK_IMAGE);


        });

        return root;
    }

    public void setBookDataToUI() {
        Book book = booksViewModel.getSelected().getValue();
        binding.manualAddBookTitle.setText(book.getTitle());
        binding.manualAddBookAuthors.setText(book.getAuthor());
        binding.manualAddBookGenres.setText(book.getGenres());
        binding.manualAddBookYear.setText(book.getYear());
        binding.manualAddBookLanguage.setText(book.getLanguages());
        binding.manualAddBookPages.setText(book.getPages());
        binding.manualAddBookSummary.setText(book.getSummary());

        Picasso.get()
                .load(book.getImage())
                .resize(1300, 2300)
                .centerCrop()
                .placeholder(R.drawable.book_cover_placeholder)
                .into(binding.manualAddBookCoverImage);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            image = selectedImageUri.toString();

            Picasso.get()
                    .load(selectedImageUri)
                    .resize(500, 700)
                    .centerCrop()
                    .placeholder(R.drawable.book_cover_placeholder)
                    .into(binding.manualAddBookCoverImage);
        }
    }

}

