package fi.mkauha.bookshelf.local;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fi.mkauha.bookshelf.models.Book;

@Database(entities = {Book.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract BookDao bookDao();


    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 2;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "book_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                BookDao dao = INSTANCE.bookDao();
                dao.deleteAll();
                Book book0 = new Book(
                        "951-1-17407-X",
                        "Moby Dick",
                        "Herman Melville",
                        "Seikkailukirjallisuus",
                        "1995",
                        "300",
                        "https://www.nauticalmind.com/wp-content/uploads/2018/04/Moby-Dick-Illustrated.jpg",
                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                                " Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. " +
                                "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. " +
                                "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                        "Suomi",
                        "Opiskelu",
                        33);
                Book book1 = new Book(
                        "9780547249643",
                        "Vuonna 1984",
                        "George Orwell",
                        "Dystopia",
                        "1995",
                        "400",
                        "https://www.oberonbooks.com/media/catalog/product/cache/1/image/650x/040ec09b1e35df139433887a97daa66f/9/7/9781783190614.jpg",
                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                                " Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. " +
                                "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. " +
                                "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                        "Suomi",
                        "Toivelista",
                        11);
                Book book2 = new Book(
                        "978-951-0-45088-8",
                        "Hohto",
                        "Stephen King",
                        "Kauhu",
                        "1995",
                        "500",
                        "http://profspevack.com/wp-content/uploads/2009/09/ADV2360_swilliams_book.jpg",
                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                                " Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. " +
                                "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. " +
                                "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                        "Suomi",
                        "Lainatut",
                        22);
                dao.insertAll(book0, book1, book2);
            });
        }
    };
}

