package fi.mkauha.bookshelf.ui.wishlist;

import android.content.Context;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.ui.books.BooksAdapter;
import fi.mkauha.bookshelf.ui.dialogs.EditBookActivity;

public class WishListFragment extends Fragment {

    public static final String SHARED_PREFS = "bookshelf_preferences";
    public static final String WISHLIST_BOOKS_KEY = "wishlist_books";

    private RecyclerView recyclerView;
    private BooksAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("ReadingListFragment", "onCreate " + this);
        super.onCreate(savedInstanceState);
        layoutManager = new GridLayoutManager(getActivity(),3);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("ReadingListFragment", "onCreateView " + this);
        View root = inflater.inflate(R.layout.fragment_wishlist, container, false);
        setHasOptionsMenu(true);

        recyclerView = (RecyclerView) root.findViewById(R.id.readingList_recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        WishListViewModel wishListViewModel = new ViewModelProvider(getActivity()).get(WishListViewModel.class);
        SharedPreferences prefs = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        wishListViewModel.init(prefs);

        mAdapter = new BooksAdapter();
        recyclerView.setAdapter(mAdapter);

        wishListViewModel.getWishListLiveData().observe(getViewLifecycleOwner(),
                list -> {
                    mAdapter.setBooksList(list);
                    mAdapter.notifyDataSetChanged();
                    recyclerView.smoothScrollToPosition(mAdapter.getItemCount() -1);
                    Log.d("BooksFragment", "Observer changed"); }
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
}