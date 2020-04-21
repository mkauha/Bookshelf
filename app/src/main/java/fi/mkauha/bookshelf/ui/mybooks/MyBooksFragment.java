package fi.mkauha.bookshelf.ui.mybooks;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.adapter.BooksAdapter;
import fi.mkauha.bookshelf.databinding.FragmentMybooksBinding;
import fi.mkauha.bookshelf.models.BookItem;
import fi.mkauha.bookshelf.ui.credits.CreditsActivity;
import fi.mkauha.bookshelf.viewmodel.BooksViewModel;
import fi.mkauha.bookshelf.viewmodel.CustomViewModelFactory;
import fi.mkauha.bookshelf.ui.details.DetailsActivity;

import static fi.mkauha.bookshelf.viewmodel.BooksViewModel.MY_BOOKS_KEY;

/**
 * Fragment that displays a owned books in a grid layout.
 *
 * Uses RecyclerView with a GridLayout to display books from ViewModel.
 * Has search and filter in top bar that can filter books by their title, author or genre.
 * Top bar also holds a button to add new books to ViewModel.
 *
 * @author  Miko Kauhanen
 * @version 1.0
 */
public class MyBooksFragment extends Fragment implements SearchView.OnQueryTextListener, SortedListAdapter.Callback {
    private FragmentMybooksBinding binding;
    private BooksViewModel booksViewModel;
    private BooksAdapter mAdapter;
    private List<BookItem> mModels;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Initializes RecyclerView, Adapter and ViewModel.
     *
     * Initializes ViewModel and gives it SharedPreferences key from which ViewModel receives the books data.
     * Initializes RecyclerView adapter that displays the books in GridLayout RecyclerView.
     * Observes changes in ViewModel to update books displayed in RecyclerView.
     *
     * @param inflater the LayoutInflater
     * @param container the ViewGroup
     * @param savedInstanceState the Bundle
     * @return root view
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("BooksFragment", "onCreateView " + this);

        binding = FragmentMybooksBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        setHasOptionsMenu(true);

        binding.booksRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        binding.booksRecyclerView.setHasFixedSize(true);

        booksViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication())).get(BooksViewModel.class);
        booksViewModel.setCurrentKey(MY_BOOKS_KEY);

        final Comparator<BookItem> alphabeticalComparator = (a, b) -> a.getTitle().compareTo(b.getTitle());

        mAdapter = new BooksAdapter(getContext(), alphabeticalComparator);
        binding.booksRecyclerView.setAdapter(mAdapter);

        booksViewModel.getMyBooksLiveData().observe(this,
            list -> {
                mModels = list;
                mAdapter.prefsKey = booksViewModel.getCurrentKey();
                mAdapter.edit()
                        .replaceAll(list)
                        .commit();
                }
        );

        return root;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem m1 = menu.findItem(R.id.app_bar_add_book);
        m1.setEnabled(true);
    }

    /**
     * When "Add" button is selected starts new details activity with "Add" -mode
     *
     * @param item selected menu item
     * @return false
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId() == R.id.app_bar_add_book) {
            Intent intent = new Intent(getActivity(), DetailsActivity.class);
            intent.putExtra("Action", "ADD");
            intent.putExtra("ViewModel_Key", booksViewModel.getCurrentKey());
            startActivity(intent);
        }
        if(item.getItemId() == R.id.app_bar_credits) {
            Intent intent = new Intent(getActivity(), CreditsActivity.class);
            startActivity(intent);
        }
        return false;
    }

    /**
     * Hides change consortium icon and set listener to search icon.
     *
     * @param menu the menu
     * @param inflater the menu inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.top_main_menu, menu);

        final MenuItem changeCityItem = menu.findItem(R.id.app_bar_change_consortium);
        changeCityItem.setVisible(false);

        final MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
    }

    /**
     * Replaces RecyclerView data with filtered list of books.
     *
     * @param query text to filter books
     * @return true
     */
    @Override
    public boolean onQueryTextChange(String query) {
        final List<BookItem> filteredModelList = filter(mModels, query);
        mAdapter.edit()
                .replaceAll(filteredModelList)
                .commit();
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    /**
     * Filters given list with given query.
     *
     * Filters books by title, author or genre and returns a list containing items that match given query.
     *
     * @param models books to filter
     * @param query text to filter books
     * @return true
     */
    private static List<BookItem> filter(List<BookItem> models, String query) {
        final String lowerCaseQuery = query.toLowerCase();

        final List<BookItem> filteredModelList = new ArrayList<>();
        for (BookItem model : models) {
            final String title = model.getTitle().toLowerCase();
            final String author = model.getAuthor().toLowerCase();
            final String genre = model.getGenre().toLowerCase();
            if (title.contains(lowerCaseQuery) || author.contains(lowerCaseQuery) || genre.contains(lowerCaseQuery)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    @Override
    public void onEditStarted() {

    }

    @Override
    public void onEditFinished() {

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}