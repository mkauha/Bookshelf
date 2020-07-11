package fi.mkauha.bookshelf.ui.books;

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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.databinding.FragmentCreateBookBinding;
import fi.mkauha.bookshelf.models.Book;
import fi.mkauha.bookshelf.ui.modals.ImageSelectModalFragment;
import fi.mkauha.bookshelf.viewmodel.BooksViewModel;
import fi.mkauha.bookshelf.viewmodel.ImageSelectViewModel;


public class CreateBookFragment extends Fragment {
    private static final String TAG = "ManualAddBook";

    private FragmentCreateBookBinding binding;
    private BooksViewModel booksViewModel;
    private ImageSelectViewModel imageSelectViewModel;
    private BottomAppBar bottomAppBar;
    private MaterialToolbar topAppBar;
    private FloatingActionButton fab;
    private Book _book;
    private String image;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentCreateBookBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        bottomAppBar = (BottomAppBar) requireActivity().findViewById(R.id.bottom_app_bar);
        topAppBar = (MaterialToolbar) requireActivity().findViewById(R.id.topAppBar);

        topAppBar.setVisibility(View.VISIBLE);

        topAppBar.setNavigationOnClickListener(event -> {
            showBackDialog();
        });

        bottomAppBar.setNavigationIcon(null);
        bottomAppBar.performShow();

        fab = requireActivity().findViewById(R.id.fab);
        fab.hide();

        bottomAppBar.replaceMenu(R.menu.menu_bottom_create_book);
        bottomAppBar.setHideOnScroll(false);

        booksViewModel = new ViewModelProvider(requireActivity()).get(BooksViewModel.class);
        imageSelectViewModel = new ViewModelProvider(requireActivity()).get(ImageSelectViewModel.class);

        imageSelectViewModel.getSelectedImageFile().observe(requireActivity(), image -> {
            Log.d(TAG, "selected: " + image);
            this.image = image;
            Glide.with(this)
                    .load(this.image)
                    .centerCrop()
                    .placeholder(R.drawable.book_cover_placeholder)
                    .into(binding.manualAddBookCoverImage);
        });

        this._book = booksViewModel.getSelected().getValue();
        if(_book != null) {
            setBookDataToUI();
        }

        ViewGroup.LayoutParams defaultParams = binding.fragmentManualAddBook.getLayoutParams();

        // TODO Add string values
        bottomAppBar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_create_book_ok:
                    showSaveDialog();
            }
            return false;
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
                    } else {
                        binding.fragmentManualAddBook.setLayoutParams(defaultParams);
                        bottomAppBar.performShow();
                    }
                });

        binding.manualAddBookCoverImage.setOnClickListener(view -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            ImageSelectModalFragment fragment = new ImageSelectModalFragment();
            fragmentTransaction.add(fragment, "BottomSheetFragment");
            fragmentTransaction.addToBackStack(null);

            fragmentTransaction.commit();
        });

        return root;
    }


    private void showBackDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.label_go_back)
                .setPositiveButton(R.string.button_ok, (dialog, which) -> {
                    NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                    navController.navigate(R.id.navigation_books);
                })
                .setNegativeButton(R.string.button_cancel, null)
                .show();
    }

    private void showSaveDialog() {

        new AlertDialog.Builder(getContext())
                .setTitle(R.string.label_save_book)
                .setPositiveButton(R.string.button_ok, (dialog, which) -> {

                    if(_book == null) {
                        _book = new Book(
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
                    } else {
                        _book.update(
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
                    }

                    booksViewModel.insertOrUpdate(_book);
                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                    navController.navigate(R.id.navigation_books);
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(R.string.button_cancel, null)
                .show();
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


        this.image = book.getImage();

        Glide.with(this)
                .load(this.image)
                .centerCrop()
                .placeholder(R.drawable.book_cover_placeholder)
                .into(binding.manualAddBookCoverImage);

    }

    @Override
    public void onStop() {
        super.onStop();
    }
}

