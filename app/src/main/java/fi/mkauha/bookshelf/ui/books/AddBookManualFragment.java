package fi.mkauha.bookshelf.ui.books;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.databinding.FragmentAddBookManualBinding;
import fi.mkauha.bookshelf.ui.dialogs.AddBookModalFragment;
import fi.mkauha.bookshelf.viewmodel.BooksViewModel;



public class AddBookManualFragment extends Fragment {

        private FragmentAddBookManualBinding binding;
        private BooksViewModel booksViewModel;
        private BottomAppBar bottomAppBar;
        private FloatingActionButton fab;


        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);
            binding = FragmentAddBookManualBinding.inflate(inflater, container, false);
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


            ViewGroup.LayoutParams defaultParams = binding.fragmentAddBookManual.getLayoutParams();

            // TODO Add string values
            bottomAppBar.setOnMenuItemClickListener(item -> {
                new AlertDialog.Builder(getContext())
                        .setTitle("Stop editing?")
                        .setMessage("Book data will be lost")
                        .setPositiveButton("Stop", (dialog, which) -> {
                            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                            NavigationUI.onNavDestinationSelected(item, navController);
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton("Continue editing", null)
                        .setIcon(R.drawable.ic_outline_cancel_24)
                        .show();
                return false;
            });

            fab.setOnClickListener(view -> {
                new AlertDialog.Builder(getContext())
                        .setTitle("Save book?")
                        .setPositiveButton("Save", (dialog, which) -> {
                            // TODO Add book to database
                            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                            navController.navigate(R.id.navigation_books);
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton("Continue editing", null)
                        .setIcon(R.drawable.ic_outline_save_24)
                        .show();
            });

            KeyboardVisibilityEvent.setEventListener(
                    getActivity(),
                    isOpen -> {
                        Log.d("AddBookManualFragment", "KEYBOARD_CHANGED: " + isOpen);
                        if(isOpen) {
                            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                                    FrameLayout.LayoutParams.MATCH_PARENT,
                                    FrameLayout.LayoutParams.MATCH_PARENT
                            );
                            params.setMargins(0, 0, 0, 55);
                            binding.fragmentAddBookManual.setLayoutParams(params);

                            bottomAppBar.performHide();
                            fab.hide();
                        } else {
                            binding.fragmentAddBookManual.setLayoutParams(defaultParams);
                            bottomAppBar.performShow();
                            fab.show();
                        }
                    });


/*            booksViewModel.getSelected().observe(getActivity(), book -> {
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
            });*/


            return root;
        }

    }

