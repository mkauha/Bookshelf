package fi.mkauha.bookshelf.viewmodel;


import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import fi.mkauha.bookshelf.data.local.model.Book;
import fi.mkauha.bookshelf.data.repository.BookRepository;

public class BooksViewModel extends AndroidViewModel {
    private static final String TAG = "BooksViewModel";

    private BookRepository mBooksRepository;
    private LiveData<List<Book>> mAllBooks;

    public BooksViewModel(Application application) {
        super(application);
        this.mBooksRepository = new BookRepository(application);
        this.mAllBooks = mBooksRepository.getLocalBooks();
    }

    public LiveData<List<Book>> getAllBooks() {
        return mAllBooks;
    }

}