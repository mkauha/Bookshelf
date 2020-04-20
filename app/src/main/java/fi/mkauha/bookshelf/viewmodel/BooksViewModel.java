package fi.mkauha.bookshelf.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

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
        return myBooksLiveData;
    }

    public LiveData<List<BookItem>> getWishListLiveData() {
        if(wishListLiveData == null) {
            wishListLiveData = new MutableLiveData<>();
        }
        return wishListLiveData;
    }

    public void loadDummyMyBooks() {
        if(myBooksRepository.isEmpty()) {
            myBooksRepository.add(new BookItem(IDGenerator.generate(context), "Moby Dick", "Herman Melville", "Seikkailukirjallisuus", "https://www.nauticalmind.com/wp-content/uploads/2018/04/Moby-Dick-Illustrated.jpg"));
            myBooksRepository.add(new BookItem(IDGenerator.generate(context), "Vuonna 1984", "George Orwell", "Dystopia", "https://s22735.pcdn.co/wp-content/uploads/1984-book-covers-2.jpg"));
            myBooksRepository.add(new BookItem(IDGenerator.generate(context), "Täällä pohjantähden alla", "Väinö Linna", "Historiallinen romaani", "https://images.gr-assets.com/books/1245092828l/1229089.jpg"));
            myBooksRepository.add(new BookItem(IDGenerator.generate(context), "Sinuhe egyptiläinen", "Mika Waltari", "Historiallinen romaani", "https://upload.wikimedia.org/wikipedia/fi/8/88/Sinuhe_egyptil%C3%A4inen.jpg"));
            myBooksRepository.add(new BookItem(IDGenerator.generate(context), "Tuntematon sotilas", "Väinö Linna", "Sotakirjallisuus", "https://upload.wikimedia.org/wikipedia/fi/7/79/Tuntematon_sotilas_kansi.jpg"));
            myBooksRepository.add(new BookItem(IDGenerator.generate(context), "Taru sormusten herrasta", "J.R.R. Tolkien", "Fantasia", "https://upload.wikimedia.org/wikipedia/fi/7/77/Taru_sormusten_herrasta.jpg"));
        }
        myBooksLiveData.setValue(myBooksRepository);
    }

    public void loadDummyWishList() {
        if(wishListRepository.isEmpty()) {
            wishListRepository.add(new BookItem(IDGenerator.generate(context), "Hohto (The Shining)", "Stephen King", "Kauhu", "http://profspevack.com/wp-content/uploads/2009/09/ADV2360_swilliams_book.jpg"));
            wishListRepository.add(new BookItem(IDGenerator.generate(context), "Kalevala", "Elias Lönnrot", "Eeppinen runous", "https://kbimages1-a.akamaihd.net/dc7bc5a1-649f-4358-b232-b38d347cabda/353/569/90/False/kalevala-22.jpg"));
        }
        wishListLiveData.setValue(wishListRepository);
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
        }
    }
}