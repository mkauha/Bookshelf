package fi.mkauha.bookshelf.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import fi.mkauha.bookshelf.models.Book;
import fi.mkauha.bookshelf.persistence.AppDatabase;
import fi.mkauha.bookshelf.persistence.BookDao;

public class BookRepository {
    private BookDao mBookDao;
    private LiveData<List<Book>> mAllBooks;

    public BookRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mBookDao = db.bookDao();
        mAllBooks = mBookDao.getAll();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<Book>> getAllWords() {
        return mAllBooks;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insert(Book book) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mBookDao.insertAll(book);
        });
    }
}
