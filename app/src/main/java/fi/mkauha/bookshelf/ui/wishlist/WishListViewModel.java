package fi.mkauha.bookshelf.ui.wishlist;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import fi.mkauha.bookshelf.items.BookItem;
import fi.mkauha.bookshelf.util.IDGenerator;
import fi.mkauha.bookshelf.util.PreferencesUtilities;

import static fi.mkauha.bookshelf.ui.wishlist.WishListFragment.WISHLIST_BOOKS_KEY;

public class WishListViewModel extends ViewModel {

    private List<BookItem> repository;
    private MutableLiveData<List<BookItem>> wishListLiveData;

    public WishListViewModel() {
        super();
        Log.d("WishListViewModel", "constructor " + this);
        repository = new ArrayList<>();
    }

    public void init(SharedPreferences sharedPreferences) {
        if(wishListLiveData == null) {
            wishListLiveData = new MutableLiveData<>();
            //sharedPreferences.registerOnSharedPreferenceChangeListener(this);

            PreferencesUtilities prefUtils = new PreferencesUtilities(sharedPreferences);
            if(!sharedPreferences.contains(WISHLIST_BOOKS_KEY)) {
                loadDummyBooks();
                prefUtils.putAll(WISHLIST_BOOKS_KEY, repository);
            } else {
                ArrayList<BookItem> myBooks =  prefUtils.getAll(WISHLIST_BOOKS_KEY);
                wishListLiveData.setValue(myBooks);
            }
        }
    }

    public LiveData<List<BookItem>> getWishListLiveData() {
        Log.d("WishListViewModel", "getAllBooks "  + this);
        if(wishListLiveData == null) {
            wishListLiveData = new MutableLiveData<>();
            loadDummyBooks();
        }
        return wishListLiveData;
    }

    public void loadDummyBooks() {
        Log.d("WishListViewModel", "loadBooks");
        if(repository.isEmpty()) {
            repository.add(new BookItem(IDGenerator.generate(), "The Shining", "Stephen King", "none", "http://profspevack.com/wp-content/uploads/2009/09/ADV2360_swilliams_book.jpg"));
        }
        wishListLiveData.setValue(repository);
        Log.d("WishListViewModel", "loadBooks " + repository.size());
    }

    public void insert(BookItem book) {
        Log.d("WishListViewModel", "insert "  + this);
        repository.add(book);
        wishListLiveData.setValue(repository);
    }

    public void update(BookItem book, int position) {

    }

    public void delete(BookItem book) {
        repository.remove(book);
    }

    public void deleteAll() {
        repository.clear();
    }

    //@Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        PreferencesUtilities prefUtils = new PreferencesUtilities(sharedPreferences);
        List<BookItem> wishListBooks =  prefUtils.getAll(s);
        wishListLiveData.setValue(wishListBooks);
        Log.d("WishListViewModel", "onSharedPreferenceChanged key: " + s + " value: " + wishListBooks);
    }
}