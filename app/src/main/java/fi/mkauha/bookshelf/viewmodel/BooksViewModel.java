package fi.mkauha.bookshelf.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import fi.mkauha.bookshelf.model.BookItem;
import fi.mkauha.bookshelf.util.IDGenerator;
import fi.mkauha.bookshelf.util.PreferencesUtilities;

import static fi.mkauha.bookshelf.ui.mybooksview.MyBooksFragment.MY_BOOKS_KEY;
import static fi.mkauha.bookshelf.ui.wishlistview.WishListFragment.WISHLIST_BOOKS_KEY;

public class BooksViewModel extends AndroidViewModel implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String SHARED_PREFS = "bookshelf_preferences";


    private SharedPreferences prefs;
    PreferencesUtilities prefUtils;
    private List<BookItem> myBooksRepository;
    private List<BookItem> wishListRepository;
    private MutableLiveData<List<BookItem>> myBooksLiveData;
    private MutableLiveData<List<BookItem>> wishListLiveData;
    private Application application;

    public BooksViewModel(Application application) {
        super(application);
        this.application = application;
        prefs = application.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        prefs.registerOnSharedPreferenceChangeListener(this);
        prefUtils = new PreferencesUtilities(prefs);

        if(myBooksLiveData == null) {
            initMyBooks(MY_BOOKS_KEY);
        }

        if(wishListLiveData == null) {
            initWishList(WISHLIST_BOOKS_KEY);
        }
        //Log.d("BooksViewModel", "prefs " + prefs.getAll());
    }

    public void initMyBooks(String prefsKey) {
        Log.d("BooksViewModel", "initMyBooks ");
        this.myBooksRepository = new ArrayList<>();
        this.myBooksLiveData = new MutableLiveData<>();

        if(!prefs.contains(prefsKey)) {
            loadDummyMyBooks();
            prefUtils.putAll(prefsKey, myBooksRepository);
        } else {
            ArrayList<BookItem> booksList =  prefUtils.getAll(prefsKey);
            myBooksLiveData.setValue(booksList);
        }
    }

    public void initWishList(String prefsKey) {
        Log.d("BooksViewModel", "initWishList");
        this.wishListRepository = new ArrayList<>();
        this.wishListLiveData = new MutableLiveData<>();

        if(!prefs.contains(prefsKey)) {
            loadDummyWishList();
            prefUtils.putAll(prefsKey, wishListRepository);
        } else {
            ArrayList<BookItem> booksList =  prefUtils.getAll(prefsKey);
            wishListLiveData.setValue(booksList);
        }
    }

    public LiveData<List<BookItem>> getMyBooksLiveData() {
        if(myBooksLiveData == null) {
            myBooksLiveData = new MutableLiveData<>();
        }
        Log.d("BooksViewModel", "getMyBooksLiveData "  + myBooksLiveData);
        return myBooksLiveData;
    }

    public LiveData<List<BookItem>> getWishListLiveData() {
        if(wishListLiveData == null) {
            wishListLiveData = new MutableLiveData<>();
        }
        Log.d("BooksViewModel", "getWishListLiveData "  + myBooksLiveData);
        return wishListLiveData;
    }

    public void loadDummyMyBooks() {
        Log.d("BooksViewModel", "loadDummyMyBooks");
        if(myBooksRepository.isEmpty()) {
            myBooksRepository.add(new BookItem(IDGenerator.generate(), "Book Title", "John McWriter", "none", "R.drawable.temp_cover_1"));
            myBooksRepository.add(new BookItem(IDGenerator.generate(), "1984", "George Orwell", "none", "https://s22735.pcdn.co/wp-content/uploads/1984-book-covers-2.jpg"));
            myBooksRepository.add(new BookItem(IDGenerator.generate(), "The Jungle Book", "Rudyard Kipling", "none", "https://i.pinimg.com/736x/d8/10/eb/d810eb142803834fa37e3ec84353ab49--the-jungle-book-book-cover-jungle-book-poster.jpg"));
            myBooksRepository.add(new BookItem(IDGenerator.generate(), "Something Nasty In The Woodshed", "Kyril Bonfiglioli", "none", "https://i1.wp.com/www.casualoptimist.com/wp-content/uploads/2014/06/9780241970270.jpg"));
        }
        myBooksLiveData.setValue(myBooksRepository);
    }

    public void loadDummyWishList() {
        Log.d("BooksViewModel", "loadDummyWishList");
        if(wishListRepository.isEmpty()) {
            wishListRepository.add(new BookItem(IDGenerator.generate(), "The Shining", "Stephen King", "none", "http://profspevack.com/wp-content/uploads/2009/09/ADV2360_swilliams_book.jpg"));
        }
        wishListLiveData.setValue(wishListRepository);
    }

    public void insert(BookItem book) {
        Log.d("BooksViewModel", "insert "  + this);
        myBooksRepository.add(book);
        myBooksLiveData.setValue(myBooksRepository);
    }

    public void update(BookItem book, int position) {

    }

    public void delete(BookItem book) {
        myBooksRepository.remove(book);
    }

    public void deleteAll() {
        myBooksRepository.clear();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        PreferencesUtilities prefUtils = new PreferencesUtilities(sharedPreferences);
        List<BookItem> myBooks =  prefUtils.getAll(s);
        switch(s) {
            case MY_BOOKS_KEY:
                myBooksLiveData.setValue(myBooks);
                break;
            case WISHLIST_BOOKS_KEY:
                wishListLiveData.setValue(myBooks);
                break;
        }
        Log.d("BooksViewModel", "onSharedPreferenceChanged prefs: " + s + ", " + sharedPreferences.getAll());
    }
}