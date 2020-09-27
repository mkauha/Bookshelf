
package fi.mkauha.bookshelf.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import fi.mkauha.bookshelf.data.local.model.Collection;
import fi.mkauha.bookshelf.data.repository.BookRepository;

public class CollectionsViewModel extends AndroidViewModel {
    private static final String TAG = "CollectionsViewModel";

    private BookRepository mBooksRepository;
    private LiveData<List<Collection>> mAllCollections;
    private MutableLiveData<Collection> mCollection = new MutableLiveData<>();

    public CollectionsViewModel(Application application) {
        super(application);
        this.mBooksRepository = new BookRepository(application);
        this.mAllCollections = mBooksRepository.getLocalCollections();
    }

    public void setCollection(Collection collection) {
        this.mCollection.setValue(collection);
    }

    public MutableLiveData<Collection> getCollection() {return mCollection;}

    public LiveData<List<Collection>> getAllCollections() {
        return mAllCollections;
    }

    public void findCollectionById(int uid) {
        Collection collection = mBooksRepository.getLocalCollectionById(uid);
        mCollection.setValue(collection);
    }

    public void insertCollection(Collection collection) {
        mBooksRepository.insertOrUpdateLocalCollection(collection);
    }

    public void deleteCollection(Collection collection) {
        mBooksRepository.deleteLocalCollection(collection);
    }
}