package fi.mkauha.bookshelf.data.local;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import fi.mkauha.bookshelf.data.local.model.Book;
import fi.mkauha.bookshelf.data.local.model.Collection;

@Dao
public interface  BookDao {
    // BOOKS
    @Query("SELECT * FROM book")
    LiveData<List<Book>> getAll();

    @Query("SELECT * FROM book WHERE title LIKE :title")
    LiveData<List<Book>> findByTitle(String title);

    @Query("SELECT * FROM book WHERE uid LIKE :uid")
    Book findById(int uid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAll(Book... books);

    @Update(entity = Book.class, onConflict = OnConflictStrategy.REPLACE)
    public int update(Book... books);

    @Delete
    public void delete(Book... books);

    @Query("DELETE FROM book")
    public void deleteAll();

    // COLLECTIONS
    @Query("SELECT * FROM collection")
    LiveData<List<Collection>> getAllCollections();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAllCollections(Collection... collections);

    @Update(entity = Collection.class, onConflict = OnConflictStrategy.REPLACE)
    public int updateCollection(Collection... collections);

    @Delete
    public void deleteCollection(Collection... collections);

    @Query("DELETE FROM collection")
    public void deleteAllCollections();
}
