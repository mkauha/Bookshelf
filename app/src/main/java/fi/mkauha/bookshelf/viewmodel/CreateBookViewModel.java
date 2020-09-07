package fi.mkauha.bookshelf.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import fi.mkauha.bookshelf.data.local.model.Book;
import fi.mkauha.bookshelf.data.repository.BookRepository;

public class CreateBookViewModel extends AndroidViewModel {
    private static final String TAG = "CreateBookViewModel";

    private BookRepository mBooksRepository;
    private MutableLiveData<Book> mBookEntity = new MutableLiveData<>();

    public CreateBookViewModel(Application application) {
        super(application);
        this.mBooksRepository = new BookRepository(application);
    }

    public void findLocalBookById(int uid) {
        Book book = mBooksRepository.getLocalBookById(uid);
        Log.d(TAG, "bookEntity: " + book);
        mBookEntity.setValue(book);
    }

    public MutableLiveData<Book> getBookEntity() {
        return mBookEntity;
    }

    public void deleteLocalBook(Book book) { mBooksRepository.deleteLocalBook(book);}

    public void insertOrUpdate(Book book) {
        mBooksRepository.insertOrUpdateLocalBook(book);
    }

}
