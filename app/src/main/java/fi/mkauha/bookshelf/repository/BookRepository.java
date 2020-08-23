package fi.mkauha.bookshelf.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import fi.mkauha.bookshelf.models.Book;
import fi.mkauha.bookshelf.network.BookResponse;
import fi.mkauha.bookshelf.network.Record;
import fi.mkauha.bookshelf.persistence.AppDatabase;
import fi.mkauha.bookshelf.persistence.BookDao;
import fi.mkauha.bookshelf.remote.ApiService;
import fi.mkauha.bookshelf.remote.ApiServiceClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookRepository {
    private static final String TAG = "BookRepository";
    private BookDao mBookDao;
    private LiveData<List<Book>> mLocalBooks;
    private final ApiService apiService;
    private List<Book> searchResults = new ArrayList<>();
    private final String[] FIELDS = {
            "cleanIsbn",
            "title",
            //"authors",
            "genres",
            "year",
            "images",
            "summary",
            "languages"
    };

    public BookRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        apiService = ApiServiceClient.getClient().create(ApiService.class);
        mBookDao = db.bookDao();
        mLocalBooks = mBookDao.getAll();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<Book>> getLocalBooks() {
        return mLocalBooks;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insertOrUpdateLocalBook(Book book) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            if(mBookDao.findById(book.getUid()).getValue() == null) {
                mBookDao.insertAll(book);
            } else {
                mBookDao.update(book);
            }
        });
    }

    public void updateLocalBook(Book book) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mBookDao.update(book);
        });

    }

    public void deleteLocalBook(Book book) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mBookDao.delete(book);
        });
    }

    public List<Book> performRemoteSearch(String query) {
        Call<BookResponse> call = apiService.getResults(query, "Title", FIELDS);
        call.enqueue(new Callback<BookResponse>() {
            @Override
            public void onResponse(Call<BookResponse>call, Response<BookResponse> response) {
                //List<Book> movies = response.body().getResults();
                Log.d(TAG, "RESPONSE: " + response);
                List<Record> recordList = response.body().getRecords();
                searchResults = new ArrayList<>();
                Log.d(TAG, "SIZE: " + recordList.size());

                for(Record record : recordList) {
                    searchResults.add(new Book(
                            record.getCleanIsbn(),
                            record.getTitle(),
                            "AUTHORS",
                            "GENRES",
                            record.getYear(),
                            "PAGES",
                            " ",
                            "SUMMARY",
                            "LANGUAGES",
                            "COLLECTION",
                            0
                    ));
                }

            }

            @Override
            public void onFailure(Call<BookResponse>call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
        return searchResults;
    }
}
