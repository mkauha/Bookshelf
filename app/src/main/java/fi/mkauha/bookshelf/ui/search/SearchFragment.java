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

import java.util.ArrayList;
import java.util.List;

import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.databinding.FragmentSearchBinding;
import fi.mkauha.bookshelf.models.Book;
import fi.mkauha.bookshelf.network.BookResponse;
import fi.mkauha.bookshelf.network.Record;
import fi.mkauha.bookshelf.remote.ApiService;
import fi.mkauha.bookshelf.remote.ApiServiceClient;
import fi.mkauha.bookshelf.ui.adapter.BookListLinearAdapter;
import fi.mkauha.bookshelf.viewmodel.BooksViewModel;
import fi.mkauha.bookshelf.viewmodel.SearchViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment  extends Fragment {
    private static final String TAG = "SearchFragment";


    private FragmentSearchBinding binding;
    BottomAppBar bottomAppBar;
    private MaterialToolbar topAppBar;
    FloatingActionButton fab;
    private BooksViewModel booksViewModel;
    private SearchViewModel searchViewModel;
    private BookListLinearAdapter mAdapter;
    private final String[] FIELDS = {
            "cleanIsbn",
            "title",
            //"authors",
            "genres",
            "year",
            "images",
            "summary",
            "languages"
    };

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
        booksViewModel = new ViewModelProvider(requireActivity()).get(BooksViewModel.class);
        booksViewModel.select(null);

        mAdapter = new BookListLinearAdapter(getContext(), booksViewModel);
        binding.booksRecyclerView.setAdapter(mAdapter);


        binding.searchTextfield.setOnEditorActionListener((textView, i, keyEvent) -> {
            Log.d(TAG, "i: " + i);
            Log.d(TAG, "keyEvent: " + keyEvent);
            doSearch(textView.getText().toString());
            return true;
        });


        return root;
    }

    private void doSearch(String query) {
        ApiService apiService = ApiServiceClient.getClient().create(ApiService.class);
        Call<BookResponse> call = apiService.getResults(query, "Title", FIELDS);
        call.enqueue(new Callback<BookResponse>() {
            @Override
            public void onResponse(Call<BookResponse>call, Response<BookResponse> response) {
                //List<Book> movies = response.body().getResults();
                Log.d(TAG, "RESPONSE: " + response);
                List<Record> recordList = response.body().getRecords();
                List<Book> searchResults = new ArrayList<>();
                Log.d(TAG, "SIZE: " + recordList.size());

                for(Record record : recordList) {
                    searchResults.add(new Book(
                            record.getCleanIsbn(),
                            record.getTitle(),
                            "AUTHORS",
                            "GENRES",
                            record.getYear(),
                            "PAGES",
                            "IMAGES",
                            "SUMMARY",
                            "LANGUAGES",
                            "COLLECTION",
                            0
                    ));
                }

                booksViewModel.setSearchResults(searchResults);

                booksViewModel.getSearchResults().observe(requireActivity(),
                        list -> {
                            mAdapter.setBooks(list);
                        }
                );

            }

            @Override
            public void onFailure(Call<BookResponse>call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }

}
