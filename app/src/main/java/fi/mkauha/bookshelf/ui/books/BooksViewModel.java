package fi.mkauha.bookshelf.ui.books;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import fi.mkauha.bookshelf.items.BookItem;

public class BooksViewModel extends ViewModel {

    private MutableLiveData<List<BookItem>> bookList = new MutableLiveData<>();

    public void setBookList(List bookList) {
        Log.d("BooksViewModel", "setBookList");
        this.bookList.setValue(bookList);
    }

    public LiveData<List<BookItem>> getBookList() {
        Log.d("BooksViewModel", "getBookList");
        return bookList;
    }

}