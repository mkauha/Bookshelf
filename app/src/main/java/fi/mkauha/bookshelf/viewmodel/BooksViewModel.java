package fi.mkauha.bookshelf.viewmodel;


import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import fi.mkauha.bookshelf.models.Book;
import fi.mkauha.bookshelf.repository.BookRepository;

public class BooksViewModel extends AndroidViewModel {

    private BookRepository mBooksRepository;
    private LiveData<List<Book>> mAllBooks;
    private MutableLiveData<List<Book>> mSearchResults = new MutableLiveData<>();
    private final MutableLiveData<Book> selected = new MutableLiveData<>();

    public BooksViewModel(Application application) {
        super(application);
        this.mBooksRepository = new BookRepository(application);
        this.mAllBooks = mBooksRepository.getLocalBooks();

    }

    public void select(Book book) {
        selected.setValue(book);
    }

    public void setSearchResults(List<Book> list) {
        mSearchResults.setValue(list);
    }

    public MutableLiveData<Book> getSelected() {
        return selected;
    }

    public LiveData<List<Book>> getSearchResults() {
        return mSearchResults;
    }

    public void performRemoteSearch(String query) {
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