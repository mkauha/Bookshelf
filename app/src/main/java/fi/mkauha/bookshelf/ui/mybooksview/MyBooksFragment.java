package fi.mkauha.bookshelf.ui.mybooksview;

import android.content.Intent;
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
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.databinding.FragmentMybooksBinding;
import fi.mkauha.bookshelf.viewmodel.BooksViewModel;
import fi.mkauha.bookshelf.viewmodel.CustomViewModelFactory;
import fi.mkauha.bookshelf.ui.details.DetailsActivity;

public class MyBooksFragment extends Fragment {
    private FragmentMybooksBinding binding;

    private RecyclerView.LayoutManager layoutManager;
    private BooksViewModel booksViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("BooksFragment", "onCreate " + this);
        super.onCreate(savedInstanceState);
        layoutManager = new GridLayoutManager(getActivity(),3);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("BooksFragment", "onCreateView " + this);

        binding = FragmentMybooksBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        setHasOptionsMenu(true);

        binding.booksRecyclerView.setLayoutManager(layoutManager);
        binding.booksRecyclerView.setHasFixedSize(true);

        booksViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication())).get(BooksViewModel.class);
        booksViewModel.setCurrentKey(BooksViewModel.MY_BOOKS_KEY);

        binding.booksRecyclerView.setAdapter(booksViewModel.getAdapter());

        booksViewModel.getMyBooksLiveData().observe(this,
            list -> {
                booksViewModel.setBooksInAdapter(list);
                binding.booksRecyclerView.smoothScrollToPosition(booksViewModel.getAdapter().getItemCount());
                }
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
            Intent intent = new Intent(getActivity(), DetailsActivity.class);
            intent.putExtra("Action", "ADD");
            intent.putExtra("ViewModel_Key", booksViewModel.getCurrentKey());
            startActivity(intent);
        }
        return false;
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