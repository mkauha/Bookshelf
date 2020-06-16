package fi.mkauha.bookshelf.persistence;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import fi.mkauha.bookshelf.models.Book;

@Dao
public interface  BookDao {
    @Query("SELECT * FROM book")
    LiveData<List<Book>> getAll();

    @Query("SELECT * FROM book WHERE title LIKE :title")
    LiveData<List<Book>> findByTitle(String title);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAll(Book... books);

    @Query("DELETE FROM book")
    public void deleteAll();
}
