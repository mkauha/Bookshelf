package fi.mkauha.bookshelf.ui.books;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.items.BookItem;
import fi.mkauha.bookshelf.ui.dialogs.AddBookActivity;

public class BooksFragment extends Fragment {

    private BooksViewModel homeViewModel;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(BooksViewModel.class);
        View root = inflater.inflate(R.layout.fragment_books, container, false);
/*        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        recyclerView = (RecyclerView) root.findViewById(R.id.books_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        setHasOptionsMenu(true);
        // this is data fro recycler view
        List itemsArray = new ArrayList();
        itemsArray.add(new BookItem(R.drawable.temp_cover_1, "Book Cover", "none", "none", "none"));
        itemsArray.add(new BookItem(R.drawable.temp_cover_2, "1984", "none", "none", "none"));
        itemsArray.add(new BookItem(R.drawable.temp_cover_3, "The Jungle Book", "none", "none", "none"));
        itemsArray.add(new BookItem(R.drawable.temp_cover_4, "Something Nasty In The Woodshed", "none", "none", "none"));
        itemsArray.add(new BookItem(R.drawable.temp_cover_4, "Something Nasty In The Woodshed", "none", "none", "none"));
        itemsArray.add(new BookItem(R.drawable.temp_cover_4, "Something Nasty In The Woodshed", "none", "none", "none"));
        itemsArray.add(new BookItem(R.drawable.temp_cover_4, "Something Nasty In The Woodshed", "none", "none", "none"));
        itemsArray.add(new BookItem(R.drawable.temp_cover_4, "Something Nasty In The Woodshed", "none", "none", "none"));
        itemsArray.add(new BookItem(R.drawable.temp_cover_4, "Something Nasty In The Woodshed", "none", "none", "none"));
        itemsArray.add(new BookItem(R.drawable.temp_cover_4, "Something Nasty In The Woodshed", "none", "none", "none"));
        itemsArray.add(new BookItem(R.drawable.temp_cover_4, "Something Nasty In The Woodshed", "none", "none", "none"));
        itemsArray.add(new BookItem(R.drawable.temp_cover_4, "Something Nasty In The Woodshed", "none", "none", "none"));


        // specify an adapter (see also next example)
        mAdapter = new BooksAdapter(itemsArray);
        recyclerView.setAdapter(mAdapter);

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
            Intent intent = new Intent(getActivity(), AddBookActivity.class);
            startActivity(intent);
        }
        return false;
    }

}