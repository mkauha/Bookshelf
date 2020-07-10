package fi.mkauha.bookshelf.ui.books;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.ui.adapter.BookListAdapter;
import fi.mkauha.bookshelf.databinding.FragmentBooksBinding;
import fi.mkauha.bookshelf.ui.modals.AddBookModalFragment;
import fi.mkauha.bookshelf.viewmodel.BooksViewModel;

public class BooksFragment extends Fragment  {
    private FragmentBooksBinding binding;
    private BooksViewModel booksViewModel;
    private BookListAdapter mAdapter;
    FloatingActionButton fab;
    BottomAppBar bottomAppBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("BooksFragment", "onCreateView " + this);

        binding = FragmentBooksBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        setHasOptionsMenu(true);
        fab = getActivity().findViewById(R.id.fab);
        fab.setImageDrawable(getActivity().getDrawable(R.drawable.ic_outline_add_24));
        fab.setOnClickListener(view -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            AddBookModalFragment fragment = new AddBookModalFragment();
            fragmentTransaction.add(fragment, "BottomSheetFragment");
            fragmentTransaction.addToBackStack(null);

            fragmentTransaction.commit();
        });

        bottomAppBar = (BottomAppBar) getActivity().findViewById(R.id.bottom_app_bar);
        bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
        bottomAppBar.replaceMenu(R.menu.menu_bottom_main);
        bottomAppBar.setNavigationIcon(R.drawable.ic_outline_menu_24);
        bottomAppBar.setOnClickListener(view -> {});
        binding.booksRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        binding.booksRecyclerView.setHasFixedSize(true);
        binding.booksRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fab.getVisibility() == View.VISIBLE) {
                    fab.hide();
                } else if (dy < 0 && fab.getVisibility() != View.VISIBLE) {
                    fab.show();
                }
            }
        });

        booksViewModel = new ViewModelProvider(getActivity()).get(BooksViewModel.class);
        booksViewModel.select(null);

        mAdapter = new BookListAdapter(getContext(), booksViewModel);
        binding.booksRecyclerView.setAdapter(mAdapter);

        booksViewModel.getAllBooks().observe(getActivity(),
            list -> mAdapter.setBooks(list)
        );

        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.menu_bottom_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("MainActivity", "onOptionsItemSelected " + item);
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        NavigationUI.onNavDestinationSelected(item, navController);

        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}