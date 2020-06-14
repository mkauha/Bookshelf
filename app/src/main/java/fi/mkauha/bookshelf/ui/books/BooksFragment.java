package fi.mkauha.bookshelf.ui.books;

import android.content.Context;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.adapter.BooksAdapter;
import fi.mkauha.bookshelf.databinding.FragmentBooksBinding;
import fi.mkauha.bookshelf.models.BookItem;
import fi.mkauha.bookshelf.ui.dialogs.AddBookModalFragment;
import fi.mkauha.bookshelf.util.IDGenerator;
import fi.mkauha.bookshelf.viewmodel.BooksViewModel;

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
public class BooksFragment extends Fragment  {
    private FragmentBooksBinding binding;
    private BooksViewModel booksViewModel;
    private BooksAdapter mAdapter;
    private List<BookItem> mModels;
    FloatingActionButton fab;
    BottomAppBar bottomAppBar;
    private List<BookItem> myBooksRepository;
    public static final String SHARED_PREFS = "bookshelf_preferences";

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

        binding = FragmentBooksBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        setHasOptionsMenu(true);
        fab = getActivity().findViewById(R.id.fab);
        fab.setImageDrawable(getActivity().getDrawable(R.drawable.ic_outline_add_24));
        fab.setOnClickListener(view -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            AddBookModalFragment fragment = new AddBookModalFragment();
            fragmentTransaction.add(fragment, "BottomSheetFragment");
            fragmentTransaction.addToBackStack(null);

            fragmentTransaction.commit();
        });

        bottomAppBar = (BottomAppBar) getActivity().findViewById(R.id.bottom_app_bar);
        bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
        bottomAppBar.replaceMenu(R.menu.bottom_main_menu);
        bottomAppBar.setNavigationIcon(R.drawable.ic_outline_menu_24);
        bottomAppBar.setOnClickListener(view -> {});
        binding.booksRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        binding.booksRecyclerView.setHasFixedSize(true);
        binding.booksRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fab.getVisibility() == View.VISIBLE) {
                    fab.hide();
                } else if (dy < 0 && fab.getVisibility() != View.VISIBLE) {
                    fab.show();
                }
            }
        });

        this.myBooksRepository = new ArrayList<>();
        booksViewModel = new ViewModelProvider(getActivity()).get(BooksViewModel.class);
        Log.d("BooksFragment: ", "booksViewModel: " + booksViewModel);
        booksViewModel.setCurrentKey(MY_BOOKS_KEY);
        loadDummyMyBooks();

        final Comparator<BookItem> alphabeticalComparator = (a, b) -> a.getTitle().compareTo(b.getTitle());

        mAdapter = new BooksAdapter(getContext(), alphabeticalComparator, booksViewModel);
        binding.booksRecyclerView.setAdapter(mAdapter);

        booksViewModel.getMyBooksLiveData().observe(getActivity(),
            list -> {
                mModels = list;
                mAdapter.prefsKey = SHARED_PREFS;
                mAdapter.edit()
                        .replaceAll(list)
                        .commit();
                }
        );

        return root;
    }

    public void loadDummyMyBooks() {
        if(myBooksRepository.isEmpty()) {
            Context context = getContext();
            myBooksRepository.add(new BookItem(IDGenerator.generate(context), "Moby Dick", "Herman Melville", "Seikkailukirjallisuus", "https://www.nauticalmind.com/wp-content/uploads/2018/04/Moby-Dick-Illustrated.jpg"));
            myBooksRepository.add(new BookItem(IDGenerator.generate(context), "Vuonna 1984", "George Orwell", "Dystopia", "https://s22735.pcdn.co/wp-content/uploads/1984-book-covers-2.jpg"));
            myBooksRepository.add(new BookItem(IDGenerator.generate(context), "Täällä pohjantähden alla", "Väinö Linna", "Historiallinen romaani", "https://images.gr-assets.com/books/1245092828l/1229089.jpg"));
            myBooksRepository.add(new BookItem(IDGenerator.generate(context), "Sinuhe egyptiläinen", "Mika Waltari", "Historiallinen romaani", "https://upload.wikimedia.org/wikipedia/fi/8/88/Sinuhe_egyptil%C3%A4inen.jpg"));
            myBooksRepository.add(new BookItem(IDGenerator.generate(context), "Tuntematon sotilas", "Väinö Linna", "Sotakirjallisuus", "https://upload.wikimedia.org/wikipedia/fi/7/79/Tuntematon_sotilas_kansi.jpg"));
            myBooksRepository.add(new BookItem(IDGenerator.generate(context), "Taru sormusten herrasta", "J.R.R. Tolkien", "Fantasia", "https://upload.wikimedia.org/wikipedia/fi/7/77/Taru_sormusten_herrasta.jpg"));
            myBooksRepository.add(new BookItem(IDGenerator.generate(context), "Hohto", "Stephen King", "Kauhu", "http://profspevack.com/wp-content/uploads/2009/09/ADV2360_swilliams_book.jpg"));
            myBooksRepository.add(new BookItem(IDGenerator.generate(context), "Kalevala", "Elias Lönnrot", "Eeppinen runous", "https://kbimages1-a.akamaihd.net/dc7bc5a1-649f-4358-b232-b38d347cabda/353/569/90/False/kalevala-22.jpg"));
        }
        booksViewModel.setBooks(myBooksRepository);
    }

/*    @Override
    public void onPrepareOptionsMenu(Menu menu) {
*//*        MenuItem m1 = menu.findItem(R.id.app_bar_add_book);
        m1.setEnabled(true);*//*
    }

    *//**
     * When "Add" button is selected starts new details activity with "Add" -mode
     *
     * @param item selected menu item
     * @return false
     *//*
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
*//*        if(item.getItemId() == R.id.app_bar_add_book) {
            Intent intent = new Intent(getActivity(), DetailsActivity.class);
            intent.putExtra("Action", "ADD");
            intent.putExtra("ViewModel_Key", booksViewModel.getCurrentKey());
            startActivity(intent);
        }
        if(item.getItemId() == R.id.app_bar_credits) {
            Intent intent = new Intent(getActivity(), CreditsActivity.class);
            startActivity(intent);
        }*//*
        return false;
    }

    *//**
     * Hides change consortium icon and set listener to search icon.
     *
     * @param menu the menu
     * @param inflater the menu inflater
     *//*
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.bottom_main_menu, menu);

*//*        final MenuItem changeCityItem = menu.findItem(R.id.app_bar_change_consortium);
        changeCityItem.setVisible(false);*//*

        final MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
    }

    *//**
     * Replaces RecyclerView data with filtered list of books.
     *
     * @param query text to filter books
     * @return true
     *//*
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

    *//**
     * Filters given list with given query.
     *
     * Filters books by title, author or genre and returns a list containing items that match given query.
     *

     * @return true
     *//*
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
*/
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