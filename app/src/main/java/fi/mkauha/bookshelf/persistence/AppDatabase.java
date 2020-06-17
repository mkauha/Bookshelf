package fi.mkauha.bookshelf.persistence;

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
                            //.addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                BookDao dao = INSTANCE.bookDao();
                dao.deleteAll();
                Book book0 = new Book(
                        "ISBN",
                        "Moby Dick",
                        "Herman Melville",
                        "Seikkailukirjallisuus",
                        "1995",
                        "300",
                        "https://www.nauticalmind.com/wp-content/uploads/2018/04/Moby-Dick-Illustrated.jpg",
                        "summary",
                        "Suomi",
                        33);
                Book book1 = new Book(
                        "ISBN",
                        "Vuonna 1984",
                        "George Orwell",
                        "Dystopia",
                        "1995",
                        "400",
                        "https://s22735.pcdn.co/wp-content/uploads/1984-book-covers-2.jpg",
                        "summary",
                        "Suomi",
                        11);
                Book book2 = new Book(
                        "ISBN",
                        "Hohto",
                        "Stephen King",
                        "Kauhu",
                        "1995",
                        "500",
                        "http://profspevack.com/wp-content/uploads/2009/09/ADV2360_swilliams_book.jpg",
                        "summary",
                        "Suomi",
                        22);
                dao.insertAll(book0, book1, book2);
            });
        }
    };
}

