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
    private LiveData<List<Record>> mSearchResults = new MutableLiveData<>();
    private Application application;

    public SearchViewModel(Application application) {
        super(application);
        this.application = application;
    }

    public void init() {
        this.mBooksRepository = new BookRepository(application);
        mSearchResults = mBooksRepository.getBooksResponseLiveData();
    }

    public LiveData<List<Record>> getSearchResults() {
        return mSearchResults;
    }

    public void searchAllRecords(String query) {
        mBooksRepository.performRemoteSearch(query);
    }

}