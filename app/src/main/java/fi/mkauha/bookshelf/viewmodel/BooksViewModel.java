package fi.mkauha.bookshelf.viewmodel;


import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import fi.mkauha.bookshelf.models.Book;
import fi.mkauha.bookshelf.repository.BookRepository;

public class BooksViewModel extends AndroidViewModel {
    private static final String TAG = "BooksViewModel";

    private BookRepository mBooksRepository;
    private LiveData<List<Book>> mAllBooks;
    private MutableLiveData<List<Book>> mSearchResults = new MutableLiveData<>();
    private final MutableLiveData<Book> selected = new MutableLiveData<>();
    private MutableLiveData<Book> mBookEntity = new MutableLiveData<>();

    public BooksViewModel(Application application) {
        super(application);
        this.mBooksRepository = new BookRepository(application);
        this.mAllBooks = mBooksRepository.getLocalBooks();
        Log.d(TAG, "mAllBooks: " + mAllBooks);

    }

    public void select(Book book) {
        selected.setValue(book);
        Log.d(TAG, "select: " + selected.getValue());
    }

    public void setSearchResults(List<Book> list) {
        mSearchResults.setValue(list);
    }

    public MutableLiveData<Book> getSelected() {
        return selected;
    }

    public void findBookById(int uid) {
        Book book = mBooksRepository.getLocalBookById(uid);
        Log.d(TAG, "bookEntity: " + book);
        mBookEntity.setValue(book);
    }

    public MutableLiveData<Book> getBookEntity() {
        return mBookEntity;
    }

    public LiveData<List<Book>> getSearchResults() {
        return mSearchResults;
    }

    public void performRemoteSearch(String query) {
        Log.d(TAG, "performRemoteSearch");
        mSearchResults.setValue(mBooksRepository.performRemoteSearch(query));
    }

    public LiveData<List<Book>> getAllBooks() {
        return mAllBooks;
    }

    public void insertOrUpdate(Book book) {
        mBooksRepository.insertOrUpdateLocalBook(book);
    }

    public void update(Book book) { mBooksRepository.updateLocalBook(book);}

    public void delete(Book book) { mBooksRepository.deleteLocalBook(book);}

}