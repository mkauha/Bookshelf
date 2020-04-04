package fi.mkauha.bookshelf.ui.wishlistview;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.viewmodel.CustomViewModelFactory;
import fi.mkauha.bookshelf.adapter.BooksAdapter;
import fi.mkauha.bookshelf.viewmodel.BooksViewModel;
import fi.mkauha.bookshelf.ui.details.EditBookActivity;

public class WishListFragment extends Fragment {

    public static final String SHARED_PREFS = "bookshelf_preferences";
    public static final String WISHLIST_BOOKS_KEY = "wishlist_books";

    private RecyclerView recyclerView;
    private BooksAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private BooksViewModel booksViewModel;
    private SharedPreferences prefs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("WishListFragment", "onCreate " + this);
        super.onCreate(savedInstanceState);
        layoutManager = new GridLayoutManager(getActivity(),3);

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("WishListFragment", "onCreateView " + this);
        View root = inflater.inflate(R.layout.fragment_wishlist, container, false);
        setHasOptionsMenu(true);

        recyclerView = (RecyclerView) root.findViewById(R.id.readingList_recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        booksViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication())).get(BooksViewModel.class);

/*        if(savedInstanceState == null) {
            booksViewModel.initWishList(WISHLIST_BOOKS_KEY);
        }*/

        mAdapter = new BooksAdapter();
        recyclerView.setAdapter(mAdapter);


        booksViewModel.getWishListLiveData().observe(this,
                list -> {
                    mAdapter.setBooksList(list);
                    mAdapter.notifyDataSetChanged();
                    //recyclerView.smoothScrollToPosition(mAdapter.getItemCount() -1);
                    Log.d("WishListFragment", "Observer changed"); }
        );

        return root;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem m1 = menu.findItem(R.id.add_book);
        m1.setEnabled(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.top_main_menu, menu);
    }
    // When menu item is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId() == R.id.add_book) {
            //Toast.makeText(getActivity(), "Add book", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getActivity(), EditBookActivity.class);
            intent.putExtra("Action", "ADD");
            startActivity(intent);
        }
        return false;
    }

    @Override
    public void onPause() {
        Log.d("WishListFragment", "onPause");
        super.onPause();
    }
}