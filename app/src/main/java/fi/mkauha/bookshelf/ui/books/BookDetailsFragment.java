package fi.mkauha.bookshelf.ui.books;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.databinding.FragmentBookdetailsBinding;
import fi.mkauha.bookshelf.models.BookItem;
import fi.mkauha.bookshelf.viewmodel.BooksViewModel;

public class BookDetailsFragment extends Fragment {

    private BooksViewModel booksViewModel;
    BottomAppBar bottomAppBar;
    FloatingActionButton fab;
    /**
     * The layout binding.
     */
    FragmentBookdetailsBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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

        bottomAppBar.replaceMenu(R.menu.bottom_add_book_menu);
        booksViewModel = new ViewModelProvider(getActivity()).get(BooksViewModel.class);

        booksViewModel.getSelected().observe(getActivity(), item -> {
            binding.bookdetailsBookTitle.setText(item.getTitle());
            binding.bookdetailsBookAuthors.setText(item.getAuthor());
            // TODO set real values to model
            binding.bookdetailsBookYear.setText("2020");
            binding.bookdetailsBookLanguage.setText("Finnish");
            binding.bookdetailsBookPages.setText("300");

            Picasso.get()
                    .load(item.getImgURL())
                    .resize(1300, 2300)
                    .centerCrop()
                    .placeholder(R.drawable.book_cover_placeholder)
                    .into(binding.bookdetailsCoverImage);
        });


        return root;
    }
}
