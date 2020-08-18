package fi.mkauha.bookshelf.models;

import androidx.annotation.NonNull;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;

import java.io.Serializable;

public class BookItem implements Serializable, SortedListAdapter.ViewModel {
    private int bookID;
    private int imageID;
    private String bookmark;
    private String title;
    private String author;
    private String genre;
    private String imgURL;

    public BookItem(int bookID, String title, String author) {
        this.bookID = bookID;
        this.title = title;
        this.author = author;
        this.bookmark = "";
    }

    public BookItem(int bookID, String title, String author, String genre) {
        this(bookID, title, author);
        this.genre = genre;
    }

    public BookItem(int bookID, String title, String author, String genre, String imgURL) {
        this(bookID, title, author, genre);
        this.imgURL = imgURL;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public String getBookmark() {
        return bookmark;
    }

    public void setBookmark(String bookmark) {
        this.bookmark = bookmark;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    @Override
    public String toString() {
        return "ID: " + bookID + " Title: " + title;
    }

    @Override
    public <T> boolean isSameModelAs(@NonNull T item) {
        if (item instanceof BookItem) {
            final BookItem other = (BookItem) item;
            return other.bookID == this.bookID;
        }
        return false;
    }

    @Override
    public <T> boolean isContentTheSameAs(@NonNull T item) {
        if (item instanceof BookItem) {
            final BookItem other = (BookItem) item;
            return this.title != null ? this.title.equals(other.getTitle()) : other.getTitle() == null;
        }
        return false;
    }
}
