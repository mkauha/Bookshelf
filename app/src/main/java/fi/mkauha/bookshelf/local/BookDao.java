package fi.mkauha.bookshelf.local;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import fi.mkauha.bookshelf.models.Book;

@Dao
public interface  BookDao {
    @Query("SELECT * FROM book")
    LiveData<List<Book>> getAll();

    @Query("SELECT * FROM book WHERE title LIKE :title")
    LiveData<List<Book>> findByTitle(String title);

    @Query("SELECT * FROM book WHERE uid LIKE :uid")
    LiveData<List<Book>> findById(int uid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAll(Book... books);

    @Update(entity = Book.class, onConflict = OnConflictStrategy.REPLACE)
    public int update(Book... books);

    @Delete
    public void delete(Book... books);

    @Query("DELETE FROM book")
    public void deleteAll();
}
