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

    private BookRepository mBooksRepository;
    private LiveData<List<Book>> mAllBooks;
    private final MutableLiveData<Book> selected = new MutableLiveData<>();

    public BooksViewModel(Application application) {
        super(application);
        this.mBooksRepository = new BookRepository(application);
        this.mAllBooks = mBooksRepository.getAllWords();

    }

    public void select(Book book) {
        Log.d("BooksViewModel", "select: " + book);
        selected.setValue(book);
    }

    public MutableLiveData<Book> getSelected() {
        return selected;
    }


    public LiveData<List<Book>> getAllBooks() {
        return mAllBooks;
    }

    public void insert(Book book) { mBooksRepository.insert(book); }

}