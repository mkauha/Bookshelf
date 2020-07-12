package fi.mkauha.bookshelf.ui.books;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.databinding.FragmentBookDetailsBinding;
import fi.mkauha.bookshelf.models.Book;
import fi.mkauha.bookshelf.viewmodel.BooksViewModel;

public class BookDetailsFragment extends Fragment {
    private static final String TAG = "BookDetailsFragment";

    private FragmentBookDetailsBinding binding;
    private BooksViewModel booksViewModel;
    private BottomAppBar bottomAppBar;
    private MaterialToolbar topAppBar;
    private FloatingActionButton fab;
    private Book _book;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentBookDetailsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        bottomAppBar = (BottomAppBar) getActivity().findViewById(R.id.bottom_app_bar);
        bottomAppBar.setNavigationIcon(null);
        bottomAppBar.performShow();
        bottomAppBar.setHideOnScroll(false);
        bottomAppBar.replaceMenu(R.menu.menu_bottom_book_details);

        topAppBar = (MaterialToolbar) requireActivity().findViewById(R.id.topAppBar);
        topAppBar.setNavigationIcon(R.drawable.ic_outline_arrow_back_24);

        topAppBar.setNavigationOnClickListener(event -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.navigation_books);
        });

        fab = requireActivity().findViewById(R.id.fab);
        fab.hide();


        booksViewModel = new ViewModelProvider(getActivity()).get(BooksViewModel.class);

        this._book = booksViewModel.getSelected().getValue();
            binding.bookDetailsBookTitle.setText(_book.getTitle());
            binding.bookDetailsBookAuthors.setText(_book.getAuthor());
            binding.bookDetailsBookYear.setText(_book.getYear());
            binding.bookDetailsBookLanguage.setText(_book.getLanguages());
            binding.bookDetailsBookPages.setText(String.valueOf(_book.getPages()));

        Glide.with(this)
                .load(_book.getImage())
                .centerCrop()
                .placeholder(R.drawable.book_cover_placeholder)
                .into(binding.bookDetailsCoverImage);

        bottomAppBar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_create_book:
                    new AlertDialog.Builder(getContext())
                            .setTitle("Edit book?")
                            .setPositiveButton("Yes", (dialog, which) -> {
                                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                                NavigationUI.onNavDestinationSelected(item, navController);
                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton("No", null)
                            .show();
                    break;
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

        binding.back.setOnClickListener(view -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.navigation_books);
        });

        binding.edit.setOnClickListener(view -> {
            new AlertDialog.Builder(getContext())
                    .setTitle("Edit book?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                        navController.navigate(R.id.navigation_create_book);
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton("No", null)
                    .show();
        });

        return root;
    }

}
