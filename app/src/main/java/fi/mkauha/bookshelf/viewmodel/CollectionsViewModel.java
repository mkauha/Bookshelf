
package fi.mkauha.bookshelf.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import fi.mkauha.bookshelf.data.local.model.Collection;
import fi.mkauha.bookshelf.data.repository.BookRepository;

public class CollectionsViewModel extends AndroidViewModel {
    private static final String TAG = "CollectionsViewModel";

    private BookRepository mBooksRepository;
    private LiveData<List<Collection>> mAllCollections;

    public CollectionsViewModel(Application application) {
        super(application);
        this.mBooksRepository = new BookRepository(application);
        this.mAllCollections = mBooksRepository.getLocalCollections();
    }

    public LiveData<List<Collection>> getAllCollections() {
        return mAllCollections;
    }

    public void insertCollection(Collection collection) {
        mBooksRepository.insertOrUpdateLocalCollection(collection);
    }

    public void deleteCollection(Collection collection) {
        mBooksRepository.deleteLocalCollection(collection);
    }
}