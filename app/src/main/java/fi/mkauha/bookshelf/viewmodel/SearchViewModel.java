package fi.mkauha.bookshelf.viewmodel;


import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import fi.mkauha.bookshelf.data.local.model.Book;
import fi.mkauha.bookshelf.data.remote.model.Record;
import fi.mkauha.bookshelf.data.repository.BookRepository;

public class SearchViewModel extends AndroidViewModel {
    private static final String TAG = "SearchViewModel";

    private BookRepository mBooksRepository;
    private LiveData<List<Book>> mAllBooks;
    private MutableLiveData<List<Record>> mSearchResults = new MutableLiveData<>();
    private final MutableLiveData<Book> selected = new MutableLiveData<>();
    private MutableLiveData<Book> mBookEntity = new MutableLiveData<>();

    public SearchViewModel(Application application) {
        super(application);
        this.mBooksRepository = new BookRepository(application);
    }

    public void select(Book book) {
        selected.setValue(book);
        Log.d(TAG, "select: " + selected.getValue());
    }

    public void setSearchResults(List<Record> list) {
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

    public LiveData<List<Record>> getSearchResults() {
        return mSearchResults;
    }

    public void performRemoteSearch(String query) {
        mSearchResults.setValue(mBooksRepository.performRemoteSearch(query));
    }

    public LiveData<List<Book>> getAllBooks() {
        return mAllBooks;
    }


}