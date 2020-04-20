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

import fi.mkauha.bookshelf.models.BookItem;
import fi.mkauha.bookshelf.util.IDGenerator;
import fi.mkauha.bookshelf.util.PreferencesUtility;

public class BooksViewModel extends AndroidViewModel implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String SHARED_PREFS = "bookshelf_preferences";
    public static final String MY_BOOKS_KEY = "my_books";
    public static final String WISHLIST_BOOKS_KEY = "wishlist_books";

    private String currentKey;

    private Context context;
    private SharedPreferences prefs;
    PreferencesUtility prefsUtils;
    private List<BookItem> myBooksRepository;
    private List<BookItem> wishListRepository;
    private MutableLiveData<List<BookItem>> myBooksLiveData;
    private MutableLiveData<List<BookItem>> wishListLiveData;

    public BooksViewModel(Application application) {
        super(application);
        prefs = application.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        prefs.registerOnSharedPreferenceChangeListener(this);
        prefsUtils = new PreferencesUtility(prefs);
        context = application.getApplicationContext();

        if(myBooksLiveData == null) {
            initMyBooks(MY_BOOKS_KEY);
        }

        if(wishListLiveData == null) {
            initWishList(WISHLIST_BOOKS_KEY);
        }
    }

    public void initMyBooks(String prefsKey) {
        Log.d("BooksViewModel", "initMyBooks ");
        this.myBooksRepository = new ArrayList<>();
        this.myBooksLiveData = new MutableLiveData<>();

        if(!prefs.contains(prefsKey)) {
            loadDummyMyBooks();
            prefsUtils.putAll(prefsKey, myBooksRepository);
        } else {
            ArrayList<BookItem> booksList =  prefsUtils.getAll(prefsKey);
            myBooksLiveData.setValue(booksList);
        }
    }

    public void initWishList(String prefsKey) {
        Log.d("BooksViewModel", "initWishList");
        this.wishListRepository = new ArrayList<>();
        this.wishListLiveData = new MutableLiveData<>();

        if(!prefs.contains(prefsKey)) {
            loadDummyWishList();
            prefsUtils.putAll(prefsKey, wishListRepository);
        } else {
            ArrayList<BookItem> booksList =  prefsUtils.getAll(prefsKey);
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
            myBooksRepository.add(new BookItem(IDGenerator.generate(context), "Moby Dick", "Herman Melville", "Adventure fiction", "https://www.nauticalmind.com/wp-content/uploads/2018/04/Moby-Dick-Illustrated.jpg"));
            myBooksRepository.add(new BookItem(IDGenerator.generate(context), "1984", "George Orwell", "Political fiction", "https://s22735.pcdn.co/wp-content/uploads/1984-book-covers-2.jpg"));
            myBooksRepository.add(new BookItem(IDGenerator.generate(context), "The Jungle Book", "Rudyard Kipling", "Children's book", "https://i.pinimg.com/736x/d8/10/eb/d810eb142803834fa37e3ec84353ab49--the-jungle-book-book-cover-jungle-book-poster.jpg"));
            myBooksRepository.add(new BookItem(IDGenerator.generate(context), "Something Nasty In The Woodshed", "Kyril Bonfiglioli", "-", "https://i1.wp.com/www.casualoptimist.com/wp-content/uploads/2014/06/9780241970270.jpg"));
        }
        myBooksLiveData.setValue(myBooksRepository);
    }

    public void loadDummyWishList() {
        Log.d("BooksViewModel", "loadDummyWishList");
        if(wishListRepository.isEmpty()) {
            wishListRepository.add(new BookItem(IDGenerator.generate(context), "The Shining", "Stephen King", "Horror", "http://profspevack.com/wp-content/uploads/2009/09/ADV2360_swilliams_book.jpg"));
        }
        wishListLiveData.setValue(wishListRepository);
    }

    public void putOne(String key, BookItem book) {
        Log.d("BooksViewModel", "putOne " + key + ", " + book.toString());
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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        PreferencesUtility prefUtils = new PreferencesUtility(sharedPreferences);
        if(!key.equals("ID")) {
            List<BookItem> myBooks =  prefUtils.getAll(key);
            switch(key) {
                case MY_BOOKS_KEY:
                    myBooksLiveData.setValue(myBooks);
                    break;
                case WISHLIST_BOOKS_KEY:
                    wishListLiveData.setValue(myBooks);
                    break;
            }
            Log.d("BooksViewModel", "onSharedPreferenceChanged in key: " + key);
        }
    }
}