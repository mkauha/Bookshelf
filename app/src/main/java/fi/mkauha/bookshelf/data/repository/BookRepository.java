package fi.mkauha.bookshelf.data.repository;

import android.app.Application;
import android.app.TaskInfo;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import fi.mkauha.bookshelf.data.local.model.Book;
import fi.mkauha.bookshelf.data.local.model.Collection;
import fi.mkauha.bookshelf.data.remote.model.BookResponse;
import fi.mkauha.bookshelf.data.remote.model.NonPresenterAuthor;
import fi.mkauha.bookshelf.data.remote.model.Record;
import fi.mkauha.bookshelf.data.local.AppDatabase;
import fi.mkauha.bookshelf.data.local.BookDao;
import fi.mkauha.bookshelf.data.remote.ApiService;
import fi.mkauha.bookshelf.data.remote.ApiServiceClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookRepository {
    private static final String TAG = "BookRepository";
    private BookDao mBookDao;
    private LiveData<List<Book>> mLocalBooks;
    private LiveData<List<Collection>> mLocalCollections;
    private final ApiService apiService;
    private MutableLiveData<List<Record>> booksResponseLiveData = new MutableLiveData<>();
    private MutableLiveData<Record> booksIdResponseLiveData = new MutableLiveData<>();
    private List<Record> recordList;
    private final String[] FILTERS = {
            "format:0/Book/"
    };
    private final String[] FIELDS = {
            "id",
            "cleanIsbn",
            "title",
            "nonPresenterAuthors",
            "genres",
            "year",
            "images",
            "summary",
            "languages",
            "physicalDescriptions"
    };

    public BookRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        apiService = ApiServiceClient.getClient().create(ApiService.class);
        mBookDao = db.bookDao();
        mLocalBooks = mBookDao.getAll();
        mLocalCollections = mBookDao.getAllCollections();
        Log.d(TAG, "mLocalCollections: " + mLocalCollections.getValue());
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<Book>> getLocalBooks() {
        return mLocalBooks;
    }

    public LiveData<List<Collection>> getLocalCollections() { return mLocalCollections; }

    public Book getLocalBookById(int uid) {
        Book book = mBookDao.findById(uid);
        Log.d(TAG, "bookfromdao: " + book);
        return book;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insertOrUpdateLocalBook(Book book) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            if(mBookDao.findById(book.getUid()) == null) {
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

    public Collection getLocalCollectionById(int uid) {
        Collection collection = mBookDao.findCollectionById(uid);
        return collection;
    }

    public void insertOrUpdateLocalCollection(Collection collection) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            if(mBookDao.findById(collection.getUid()) == null) {
                mBookDao.insertAllCollections(collection);
            } else {
                mBookDao.updateCollection(collection);
            }
        });
    }

    public void updateLocalCollection(Collection collection) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mBookDao.updateCollection(collection);
        });

    }

    public void deleteLocalCollection(Collection collection) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mBookDao.deleteCollection(collection);
        });
    }

    public void performRemoteSearch(String query) {
        Call<BookResponse> call = apiService.getAllRecords(query, "Title", FILTERS, FIELDS);
        call.enqueue(new Callback<BookResponse>() {
            @Override
            public void onResponse(Call<BookResponse>call, Response<BookResponse> response) {

                if(response.body() != null) {
                    //Log.d(TAG, "RESPONSE: " + response);
                    recordList = response.body().getRecords();
                    Log.d(TAG, "SIZE: " + recordList.size());

                    for (Record record : recordList) {
                        String imageURL = " ";
                        String author = "-";
                        String language = "-";
                        String summary = "-";
                        String genres = "-";
                        String physicalDescriptions = "-";

                        if (!record.getImages().isEmpty()) {
                            imageURL = "https://api.finna.fi" + record.getImages().get(0);
                            //Log.d(TAG, "IMAGE:" + imageURL);
                        }
                        record.getImages().add(imageURL);

                        if (!record.getNonPresenterAuthors().isEmpty()) {
                            author = record.getNonPresenterAuthors().get(0).getName();
                        }
                        NonPresenterAuthor nonPresenterAuthor = new NonPresenterAuthor();
                        nonPresenterAuthor.setName(author);
                        record.getNonPresenterAuthors().add(nonPresenterAuthor);

                        if (!record.getLanguages().isEmpty()) {
                            language = record.getLanguages().get(0);
                            // Log.d(TAG, "LANG:" + language);
                        }
                        record.getLanguages().add(language);


                        if (!record.getSummary().isEmpty()) {
                            summary = record.getSummary().get(0);
                            //Log.d(TAG, "SUMMARY:" + summary);
                        }
                        record.getSummary().add(summary);


                        if (!record.getGenres().isEmpty()) {
                            genres = record.getGenres().get(0);
                            //Log.d(TAG, "SUMMARY:" + genres);
                        }
                        record.getGenres().add(genres);


                        if (!record.getPhysicalDescriptions().isEmpty()) {
                            physicalDescriptions = record.getPhysicalDescriptions().get(0);
                            //Log.d(TAG, "SUMMARY:" + genres);
                        }
                        record.getPhysicalDescriptions().add(physicalDescriptions);


                    }

                    booksResponseLiveData.postValue(recordList);
                }
            }

            @Override
            public void onFailure(Call<BookResponse>call, Throwable t) {
                // Log error here since request failed
                booksResponseLiveData.postValue(null);
                Log.e(TAG, t.toString());
            }
        });
    }

    public void performRemoteSearchById(String id) {
        Log.d(TAG, "performRemoteSearchById: " + id);
        Call<BookResponse> call = apiService.getRecordById(id, FILTERS, FIELDS);
        call.enqueue(new Callback<BookResponse>() {
            @Override
            public void onResponse(Call<BookResponse>call, Response<BookResponse> response) {
                if(response.body() != null) {
                    Log.d(TAG, "RESPONSE: " + response);
                    recordList = response.body().getRecords();
                    Log.d(TAG, "SIZE: " + recordList.size());

                    for (Record record : recordList) {
                        String imageURL = " ";
                        String author = "-";
                        String language = "-";
                        String summary = "-";
                        String genres = "-";
                        String physicalDescriptions = "-";

                        if (!record.getImages().isEmpty()) {
                            imageURL = "https://api.finna.fi" + record.getImages().get(0);
                            //Log.d(TAG, "IMAGE:" + imageURL);
                        }
                        record.getImages().add(imageURL);

                        if (!record.getNonPresenterAuthors().isEmpty()) {
                            author = record.getNonPresenterAuthors().get(0).getName();
                        }
                        NonPresenterAuthor nonPresenterAuthor = new NonPresenterAuthor();
                        nonPresenterAuthor.setName(author);
                        record.getNonPresenterAuthors().add(nonPresenterAuthor);

                        if (!record.getLanguages().isEmpty()) {
                            language = record.getLanguages().get(0);
                            // Log.d(TAG, "LANG:" + language);
                        }
                        record.getLanguages().add(language);


                    if(!record.getSummary().isEmpty() || record.getSummary() == null) {
                        summary = record.getSummary().get(0);
                        //Log.d(TAG, "SUMMARY:" + summary);
                    }
                        record.setSummary(new ArrayList<>());
                        record.getSummary().add(summary);


                        if (!record.getGenres().isEmpty()) {
                            genres = record.getGenres().get(0);
                            //Log.d(TAG, "SUMMARY:" + genres);
                        }
                        record.getGenres().add(genres);


                        if (!record.getPhysicalDescriptions().isEmpty()) {
                            physicalDescriptions = record.getPhysicalDescriptions().get(0);
                            //Log.d(TAG, "SUMMARY:" + genres);
                        }
                        record.getPhysicalDescriptions().add(physicalDescriptions);

                    }

                    booksIdResponseLiveData.setValue(recordList.get(0));
                }
            }

            @Override
            public void onFailure(Call<BookResponse>call, Throwable t) {
                booksResponseLiveData.setValue(null);
                Log.e(TAG, t.toString());
            }
        });
    }

    public LiveData<List<Record>> getBooksResponseLiveData() {
        return booksResponseLiveData;
    }

    public LiveData<Record> getBooksIdResponseLiveData() {
        return booksIdResponseLiveData;
    }
}
