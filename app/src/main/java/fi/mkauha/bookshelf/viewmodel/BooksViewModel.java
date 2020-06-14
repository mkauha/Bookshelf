package fi.mkauha.bookshelf.viewmodel;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import fi.mkauha.bookshelf.models.BookItem;
import fi.mkauha.bookshelf.util.PreferencesUtility;

public class BooksViewModel extends ViewModel {

    public static final String MY_BOOKS_KEY = "my_books";
    public static final String WISHLIST_BOOKS_KEY = "wishlist_books";

    private String currentKey;
    PreferencesUtility prefsUtils;
    private List<BookItem> myBooksRepository;
    private MutableLiveData<List<BookItem>> myBooksLiveData;
    private final MutableLiveData<BookItem> selected = new MutableLiveData<BookItem>();

    public BooksViewModel() {
        super();
        currentKey = MY_BOOKS_KEY;
        this.myBooksLiveData = new MutableLiveData<>();
    }

    public void select(BookItem item) {
        Log.d("BooksViewModel", "select: " + item);
        selected.setValue(item);
    }

    public MutableLiveData<BookItem> getSelected() {
        return selected;
    }


    public LiveData<List<BookItem>> getMyBooksLiveData() {
        if(myBooksLiveData == null) {
            myBooksLiveData = new MutableLiveData<>();
        }
        return myBooksLiveData;
    }

    public void setBooks(List<BookItem> myBooksRepository) {
        myBooksLiveData.setValue(myBooksRepository);
    }

    public MutableLiveData<List<BookItem>> getBooks() {
        return this.myBooksLiveData;
    }

    public void putOne(String key, BookItem book) {
        prefsUtils.putOne(key, book);
    }

    public BookItem getOne(String key, int id) {
        return prefsUtils.getOne(key, id);
    }

    public void updateOne(String key, BookItem book ) {
        prefsUtils.updateOne(key, book);
    }

    public void removeOne(String key, int id) {
        prefsUtils.removeOne(key, id);
    }

    public void deleteAll() {
        myBooksRepository.clear();
    }

    public void setCurrentKey(String key) {
        this.currentKey = key;
    }

    public String getCurrentKey() {
        return this.currentKey;
    }


}