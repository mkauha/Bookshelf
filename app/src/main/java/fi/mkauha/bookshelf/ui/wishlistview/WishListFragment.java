package fi.mkauha.bookshelf.ui.wishlistview;

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

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.adapter.BooksAdapter;
import fi.mkauha.bookshelf.databinding.FragmentWishlistBinding;
import fi.mkauha.bookshelf.model.BookItem;
import fi.mkauha.bookshelf.viewmodel.CustomViewModelFactory;
import fi.mkauha.bookshelf.viewmodel.BooksViewModel;
import fi.mkauha.bookshelf.ui.details.DetailsActivity;

public class WishListFragment extends Fragment implements SearchView.OnQueryTextListener, SortedListAdapter.Callback {
    private FragmentWishlistBinding binding;
    private RecyclerView.LayoutManager layoutManager;
    private BooksViewModel booksViewModel;
    BooksAdapter mAdapter;
    private List<BookItem> mModels;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("WishListFragment", "onCreate " + this);
        super.onCreate(savedInstanceState);
        //layoutManager = new GridLayoutManager(getActivity(),3);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("WishListFragment", "onCreateView " + this);
        binding = FragmentWishlistBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        setHasOptionsMenu(true);


        binding.wishListRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        binding.wishListRecyclerView.setHasFixedSize(true);

        booksViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication())).get(BooksViewModel.class);
        booksViewModel.setCurrentKey(BooksViewModel.WISHLIST_BOOKS_KEY);


        final Comparator<BookItem> alphabeticalComparator = (a, b) -> a.getTitle().compareTo(b.getTitle());

        mAdapter = new BooksAdapter(getContext(), alphabeticalComparator);
        binding.wishListRecyclerView.setAdapter(mAdapter);


        booksViewModel.getWishListLiveData().observe(this,
                list -> {
                    mModels = list;
                    mAdapter.prefsKey = booksViewModel.getCurrentKey();
                    mAdapter.edit()
                            .replaceAll(list)
                            .commit();
                    }
        );

        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem m1 = menu.findItem(R.id.add_book);
        m1.setEnabled(true);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId() == R.id.add_book) {
            Intent intent = new Intent(getActivity(), DetailsActivity.class);
            intent.putExtra("Action", "ADD");
            intent.putExtra("ViewModel_Key", booksViewModel.getCurrentKey());
            startActivity(intent);
        }
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.top_main_menu, menu);

        final MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
    }

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