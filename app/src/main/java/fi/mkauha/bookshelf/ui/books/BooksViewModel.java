package fi.mkauha.bookshelf.ui.books;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.items.BookItem;
import fi.mkauha.bookshelf.util.PreferencesUtilities;

import static fi.mkauha.bookshelf.ui.books.BooksFragment.MY_BOOKS_KEY;

public class BooksViewModel extends ViewModel implements SharedPreferences.OnSharedPreferenceChangeListener {

    private List<BookItem> repository;
    private MutableLiveData<List<BookItem>> myBooksLiveData;

    public BooksViewModel() {
        super();
        Log.d("BooksViewModel", "constructor " + this);
        repository = new ArrayList<>();
    }

    public void init(SharedPreferences sharedPreferences) {
        if(myBooksLiveData == null) {
            myBooksLiveData = new MutableLiveData<>();
            sharedPreferences.registerOnSharedPreferenceChangeListener(this);

            PreferencesUtilities prefUtils = new PreferencesUtilities(sharedPreferences);
            if(!sharedPreferences.contains(MY_BOOKS_KEY)) {
                loadDummyBooks();
                prefUtils.putAll(MY_BOOKS_KEY, repository);
            } else {
                ArrayList<BookItem> myBooks =  prefUtils.getAll(MY_BOOKS_KEY);
                myBooksLiveData.setValue(myBooks);
            }
        }
    }

    public LiveData<List<BookItem>> getMyBooksLiveData() {
        Log.d("BooksViewModel", "getAllBooks "  + this);
        if(myBooksLiveData == null) {
            myBooksLiveData = new MutableLiveData<>();
            loadDummyBooks();
        }
        return myBooksLiveData;
    }

    public void loadDummyBooks() {
        Log.d("BooksViewModel", "loadBooks");
        if(repository.isEmpty()) {
            repository.add(new BookItem(R.drawable.temp_cover_1, "Book Title", "John McWriter", "none", "R.drawable.temp_cover_1"));
            repository.add(new BookItem(R.drawable.temp_cover_2, "1984", "George Orwell", "none", "https://s22735.pcdn.co/wp-content/uploads/1984-book-covers-2.jpg"));
            repository.add(new BookItem(R.drawable.temp_cover_3, "The Jungle Book", "Rudyard Kipling", "none", "https://i.pinimg.com/736x/d8/10/eb/d810eb142803834fa37e3ec84353ab49--the-jungle-book-book-cover-jungle-book-poster.jpg"));
            repository.add(new BookItem(R.drawable.temp_cover_4, "Something Nasty In The Woodshed", "Kyril Bonfiglioli", "none", "https://i1.wp.com/www.casualoptimist.com/wp-content/uploads/2014/06/9780241970270.jpg"));
        }
        myBooksLiveData.setValue(repository);
        Log.d("BooksViewModel", "loadBooks " + repository.size());
    }

    public void insert(BookItem book) {
        Log.d("BooksViewModel", "insert "  + this);
        repository.add(book);
        myBooksLiveData.setValue(repository);
    }

    public void update(BookItem book, int position) {

    }

    public void delete(BookItem book) {
        repository.remove(book);
    }

    public void deleteAll() {
        repository.clear();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        PreferencesUtilities prefUtils = new PreferencesUtilities(sharedPreferences);
        List<BookItem> myBooks =  prefUtils.getAll(s);
        myBooksLiveData.setValue(myBooks);
        Log.d("BooksViewModel", "onSharedPreferenceChanged key: " + s + " value: " + myBooks);
    }
}