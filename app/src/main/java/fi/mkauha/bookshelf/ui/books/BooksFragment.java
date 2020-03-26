package fi.mkauha.bookshelf.ui.books;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.ui.dialogs.EditBookActivity;

public class BooksFragment extends Fragment {

    public static final String SHARED_PREFS = "bookshelf_preferences";
    public static final String MY_BOOKS_KEY = "my_books";

    private RecyclerView recyclerView;
    private BooksAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("BooksFragment", "onCreate " + this);
        super.onCreate(savedInstanceState);
        layoutManager = new LinearLayoutManager(getActivity());
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("BooksFragment", "onCreateView " + this);

        View root = inflater.inflate(R.layout.fragment_books, container, false);
        setHasOptionsMenu(true);

        recyclerView = (RecyclerView) root.findViewById(R.id.books_recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        BooksViewModel booksViewModel = new ViewModelProvider(getActivity()).get(BooksViewModel.class);
        SharedPreferences prefs = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        booksViewModel.init(prefs);

        mAdapter = new BooksAdapter();
        recyclerView.setAdapter(mAdapter);

        booksViewModel.getMyBooksLiveData().observe(getViewLifecycleOwner(),
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

    @Override
    public void onStart() {
        super.onStart();
        //
    }

    @Override
    public void onStop() {
        Log.d("BooksFragment", "onPause");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.d("BooksFragment", "onDestroyView");
        super.onDestroyView();
    }
}