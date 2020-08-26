package fi.mkauha.bookshelf.ui.search;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.databinding.FragmentSearchBinding;
import fi.mkauha.bookshelf.ui.adapter.BookListLinearAdapter;
import fi.mkauha.bookshelf.viewmodel.BooksViewModel;
import fi.mkauha.bookshelf.viewmodel.SearchViewModel;

public class SearchFragment  extends Fragment {
    private static final String TAG = "SearchFragment";


    private FragmentSearchBinding binding;
    BottomAppBar bottomAppBar;
    private MaterialToolbar topAppBar;
    FloatingActionButton fab;
    private SearchViewModel searchViewModel;
    private BookListLinearAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        bottomAppBar = requireActivity().findViewById(R.id.bottom_app_bar);
        bottomAppBar.replaceMenu(R.menu.menu_bottom_search);

        fab = requireActivity().findViewById(R.id.fab);
        fab.hide();

        binding.toolbar.setNavigationOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.navigation_books);
        });

        binding.booksRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.booksRecyclerView.setHasFixedSize(true);
        searchViewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
        searchViewModel.select(null);

        mAdapter = new BookListLinearAdapter(getContext(), searchViewModel);
        binding.booksRecyclerView.setAdapter(mAdapter);


        binding.searchTextfield.setOnEditorActionListener((textView, i, keyEvent) -> {
            Log.d(TAG, "query: " + textView.getText().toString());
            searchViewModel.performRemoteSearch(textView.getText().toString());
            return true;
        });

        searchViewModel.getSearchResults().observe(requireActivity(),
                list -> {
                    //Log.d(TAG, "mAdapter.setBooks " + list);
                    mAdapter.setBooks(list);
                }
        );


        return root;
    }

}
