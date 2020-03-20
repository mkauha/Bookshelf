package fi.mkauha.bookshelf.ui.books;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.items.BookItem;
import fi.mkauha.bookshelf.ui.dialogs.AddBookActivity;
import fi.mkauha.bookshelf.ui.dialogs.EditBookActivity;

public class BooksFragment extends Fragment {

    private BooksViewModel homeViewModel;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<BookItem> bookList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeViewModel = new ViewModelProvider(getActivity()).get(BooksViewModel.class);

        // this is data for recycler view
        //TODO get items from file
        bookList = new ArrayList<>();
        if(bookList.isEmpty()) {
            bookList.add(new BookItem(R.drawable.temp_cover_1, "Book Title", "John McWriter", "none", "R.drawable.temp_cover_1"));
            bookList.add(new BookItem(R.drawable.temp_cover_2, "1984", "George Orwell", "none", "https://s22735.pcdn.co/wp-content/uploads/1984-book-covers-2.jpg"));
            bookList.add(new BookItem(R.drawable.temp_cover_3, "The Jungle Book", "Rudyard Kipling", "none", "https://i.pinimg.com/736x/d8/10/eb/d810eb142803834fa37e3ec84353ab49--the-jungle-book-book-cover-jungle-book-poster.jpg"));
            bookList.add(new BookItem(R.drawable.temp_cover_4, "Something Nasty In The Woodshed", "Kyril Bonfiglioli", "none", "https://i1.wp.com/www.casualoptimist.com/wp-content/uploads/2014/06/9780241970270.jpg"));
            homeViewModel.setBookList(bookList);
        }


    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_books, container, false);


        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver, new IntentFilter("AddBookActivity"));

        recyclerView = (RecyclerView) root.findViewById(R.id.books_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        setHasOptionsMenu(true);

        // specify an adapter (see also next example)
        mAdapter = new BooksAdapter(bookList);
        recyclerView.setAdapter(mAdapter);

        homeViewModel.getBookList().observe(getViewLifecycleOwner(),
            list -> {
                this.bookList = list;
                mAdapter.notifyItemInserted(mAdapter.getItemCount());
                mAdapter.notifyDataSetChanged();
            // https://stackoverflow.com/questions/26635841/recyclerview-change-data-set
            //mAdapter.notifyDataSetChanged();
            //recyclerView.setAdapter(mAdapter); }
                Log.d("homeViewModel", "fetch"); }
        );

        return root;
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {

        int temperature;

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("BooksFragment", "onReceive");
            Bundle bundle = intent.getExtras();
            BookItem item = (BookItem) bundle.getSerializable("BOOK_ITEM");
            addListItem(item);
            homeViewModel.setBookList(bookList);
            recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount() -1);
        }
    };

    private void addListItem(BookItem item) {
        //TODO add to file and read file
        bookList.add(item);
        recyclerView.getAdapter().notifyDataSetChanged();
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