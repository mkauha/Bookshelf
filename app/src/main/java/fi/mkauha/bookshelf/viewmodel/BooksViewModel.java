package fi.mkauha.bookshelf.viewmodel;


import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import fi.mkauha.bookshelf.data.local.model.Book;
import fi.mkauha.bookshelf.data.local.model.Collection;
import fi.mkauha.bookshelf.data.repository.BookRepository;

public class BooksViewModel extends AndroidViewModel {
    private static final String TAG = "BooksViewModel";

    private BookRepository mBooksRepository;
    private LiveData<List<Book>> mAllBooks;
    private LiveData<List<Collection>> mAllCollections;

    public BooksViewModel(Application application) {
        super(application);
        this.mBooksRepository = new BookRepository(application);
        this.mAllBooks = mBooksRepository.getLocalBooks();
        this.mAllCollections = mBooksRepository.getLocalCollections();
    }

    public LiveData<List<Book>> getAllBooks() {
        return mAllBooks;
    }

    public LiveData<List<Collection>> getAllCollections() {
        return mAllCollections;
    }

    public void insertCollection(Collection collection) {
        mBooksRepository.insertOrUpdateLocalCollection(collection);
    }
}