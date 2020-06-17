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

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.databinding.FragmentBookdetailsBinding;
import fi.mkauha.bookshelf.viewmodel.BooksViewModel;

public class BookDetailsFragment extends Fragment {

    private FragmentBookdetailsBinding binding;
    private BooksViewModel booksViewModel;
    private BottomAppBar bottomAppBar;
    private FloatingActionButton fab;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentBookdetailsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        bottomAppBar = (BottomAppBar) getActivity().findViewById(R.id.bottom_app_bar);
        bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);

        bottomAppBar.setNavigationIcon(null);
        bottomAppBar.performShow();

        fab = getActivity().findViewById(R.id.fab);
        fab.setImageDrawable(getActivity().getDrawable(R.drawable.ic_outline_arrow_back_ios_24));
        fab.show();

        fab.setOnClickListener(view -> {
            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.navigation_books);
        });

        bottomAppBar.replaceMenu(R.menu.bottom_book_details_menu);
        booksViewModel = new ViewModelProvider(getActivity()).get(BooksViewModel.class);

        booksViewModel.getSelected().observe(getActivity(), book -> {
            binding.bookdetailsBookTitle.setText(book.getTitle());
            binding.bookdetailsBookAuthors.setText(book.getAuthor());
            binding.bookdetailsBookYear.setText(book.getYear());
            binding.bookdetailsBookLanguage.setText(book.getLanguages());
            binding.bookdetailsBookPages.setText(String.valueOf(book.getPages()));

            Picasso.get()
                    .load(book.getImage())
                    .resize(1300, 2300)
                    .centerCrop()
                    .placeholder(R.drawable.book_cover_placeholder)
                    .into(binding.bookdetailsCoverImage);
        });

        bottomAppBar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_manual_add:
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
                case R.id.bookdetails_share:
                    Log.d("ManualAddBookFragment", "Share");
                    break;
                case R.id.bookdetails_favorite:
                    Log.d("ManualAddBookFragment", "Favorite");
                    break;
                case R.id.bookdetails_bookmark:
                    Log.d("ManualAddBookFragment", "Bookmark");
                    break;
            }
            return false;
        });


        return root;
    }

}
