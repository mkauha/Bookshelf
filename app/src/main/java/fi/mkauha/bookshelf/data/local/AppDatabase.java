package fi.mkauha.bookshelf.data.local;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.data.local.model.Book;
import fi.mkauha.bookshelf.data.local.model.Collection;

@Database(entities = {Book.class, Collection.class}, version = 1)
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
                            .allowMainThreadQueries()
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
                        "https://external-content.duckduckgo.com/iu/?u=http%3A%2F%2Fimage.anobii.com%2Fanobi%2Fimage_book.php%3Fitem_id%3D0105e5bbc6522d7840&f=1&nofb=1",
                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                                " Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. " +
                                "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. " +
                                "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                        "Suomi",
                        "Toivelista",
                        11);
                Book book2 = new Book(
                        "978-951-0-45088-8",
                        "Dune",
                        "Frank Herbert",
                        "Scifi",
                        "1995",
                        "500",
                        "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fcdn1.thr.com%2Fsites%2Fdefault%2Ffiles%2Fimagecache%2FNFE_portrait%2F2019%2F04%2Fdune_hc-p_2019.jpg&f=1&nofb=1",
                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                                " Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. " +
                                "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. " +
                                "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                        "English",
                        "Lainatut",
                        22);
                dao.insertAll(book0, book1, book2);

                dao.deleteAllCollections();


                Collection collection0 = new Collection("All", 0);
                Collection collection1 = new Collection("Wishlist", 1);
                Collection collection2 = new Collection("Study", 2);

                // ALL
                collection0.add(book0);
                collection0.add(book1);
                collection0.add(book2);
                // Wishlist
                collection1.add(book2);
                // Study
                collection2.add(book1);

                Log.d("AppDatabase", "collection0: " + collection0.toString());

                dao.insertAllCollections(collection0, collection1, collection2);

            });
        }
    };
}

