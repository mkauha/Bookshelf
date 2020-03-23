package fi.mkauha.bookshelf.ui.books;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
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
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.items.BookItem;
import fi.mkauha.bookshelf.ui.dialogs.EditBookActivity;

public class BooksFragment extends Fragment {

    private BooksViewModel booksViewModel;
    private RecyclerView recyclerView;
    private BooksAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<BookItem> bookList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("BooksFragment", "onCreate " + this);
        super.onCreate(savedInstanceState);
        layoutManager = new LinearLayoutManager(getActivity());
      //TODO get items from file
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("BooksFragment", "onCreateView " + this);

        View root = inflater.inflate(R.layout.fragment_books, container, false);
        setHasOptionsMenu(true);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver, new IntentFilter("AddBookActivity"));

        recyclerView = (RecyclerView) root.findViewById(R.id.books_recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        booksViewModel = new ViewModelProvider(getActivity()).get(BooksViewModel.class);

        mAdapter = new BooksAdapter();
        recyclerView.setAdapter(mAdapter);


        booksViewModel.getAllBooks().observe(getViewLifecycleOwner(),
            list -> {
                mAdapter.setBooksList(list);
                mAdapter.notifyDataSetChanged();
            // https://stackoverflow.com/questions/26635841/recyclerview-change-data-set
                Log.d("observer", "Changed"); }
        );

        return root;
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("BooksFragment", "onReceive " + this);
            Bundle bundle = intent.getExtras();
            BookItem bookItem = (BookItem) bundle.getSerializable("BOOK_ITEM");
            //addListItem(item);
            booksViewModel.insert(bookItem);
            recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount() -1);
        }
    };

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