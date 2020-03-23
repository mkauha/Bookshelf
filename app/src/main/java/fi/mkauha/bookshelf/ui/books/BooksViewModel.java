package fi.mkauha.bookshelf.ui.books;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import fi.mkauha.bookshelf.R;
import fi.mkauha.bookshelf.items.BookItem;

public class BooksViewModel extends ViewModel {
    private List<BookItem> repository;
    private MutableLiveData<List<BookItem>> allBooks;

    public BooksViewModel() {
        Log.d("BooksViewModel", "constructor " + this);
        repository = new ArrayList<>();
    }

    public LiveData<List<BookItem>> getAllBooks() {
        Log.d("BooksViewModel", "getAllBooks "  + this);
        if(allBooks == null) {
            allBooks = new MutableLiveData<>();
            loadBooks();
        }
        return allBooks;
    }

    public void loadBooks() {
        Log.d("BooksViewModel", "loadBooks");
        if(repository.isEmpty()) {
            repository.add(new BookItem(R.drawable.temp_cover_1, "Book Title", "John McWriter", "none", "R.drawable.temp_cover_1"));
            repository.add(new BookItem(R.drawable.temp_cover_2, "1984", "George Orwell", "none", "https://s22735.pcdn.co/wp-content/uploads/1984-book-covers-2.jpg"));
            repository.add(new BookItem(R.drawable.temp_cover_3, "The Jungle Book", "Rudyard Kipling", "none", "https://i.pinimg.com/736x/d8/10/eb/d810eb142803834fa37e3ec84353ab49--the-jungle-book-book-cover-jungle-book-poster.jpg"));
            repository.add(new BookItem(R.drawable.temp_cover_4, "Something Nasty In The Woodshed", "Kyril Bonfiglioli", "none", "https://i1.wp.com/www.casualoptimist.com/wp-content/uploads/2014/06/9780241970270.jpg"));
        }
        allBooks.setValue(repository);
    }

    public void insert(BookItem book) {
        Log.d("BooksViewModel", "insert "  + this);
        repository.add(book);
        List<BookItem> oldList = allBooks.getValue();
        ArrayList<BookItem> clonedBooks;
        if (oldList == null) {
            clonedBooks = new ArrayList<>();
        } else {
            clonedBooks = new ArrayList<>(oldList);
        }
        clonedBooks.add(book);
        allBooks.setValue(clonedBooks);
    }

    public void update(BookItem book) {

    }

    public void delete(BookItem book) {
        repository.remove(book);
    }

    public void deleteAllNotes() {
        repository.clear();
    }
}