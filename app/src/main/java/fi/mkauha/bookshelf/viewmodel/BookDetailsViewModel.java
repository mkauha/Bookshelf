package fi.mkauha.bookshelf.viewmodel;


import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import fi.mkauha.bookshelf.data.local.model.Book;
import fi.mkauha.bookshelf.data.remote.model.Record;
import fi.mkauha.bookshelf.data.repository.BookRepository;

public class BookDetailsViewModel extends AndroidViewModel {
    private static final String TAG = "BookDetailsViewModel";

    private BookRepository mBooksRepository;
    private LiveData<Record> mSearchResults = new MutableLiveData<>();
    private MutableLiveData<Book> mBookEntity = new MutableLiveData<>();
    private Application application;

    public BookDetailsViewModel(Application application) {
        super(application);
        this.application = application;
    }

    public void init() {
        this.mBooksRepository = new BookRepository(application);
        mSearchResults = mBooksRepository.getBooksIdResponseLiveData();
    }

    public void searchRecordById(String id) {
        mBooksRepository.performRemoteSearchById(id);
        Log.d(TAG, "mSearchResults: " + mSearchResults.getValue());
    }

    public LiveData<Record> getSearchResults() {
        return mSearchResults;
    }

    public void findLocalBookById(int uid) {
        Book book = mBooksRepository.getLocalBookById(uid);
        Log.d(TAG, "bookEntity: " + book);
        mBookEntity.setValue(book);
    }

    public void deleteLocalBook(Book book) { mBooksRepository.deleteLocalBook(book);}

    public MutableLiveData<Book> getBookEntity() {
        return mBookEntity;
    }
}