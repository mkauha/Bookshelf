package fi.mkauha.bookshelf.models;

import androidx.annotation.NonNull;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;

import java.io.Serializable;

/**
 * BookItem POJO-class.
 *
 * @author  Miko Kauhanen
 * @version 1.0
 */
public class BookItem implements Serializable, SortedListAdapter.ViewModel {
    private int bookID;
    private int imageID;
    private String bookmark;
    private String title;
    private String author;
    private String genre;
    private String imgURL;

    /**
     * Instantiates a new Book item.
     *
     * @param bookID the book id
     * @param title  the title
     * @param author the author
     */
    public BookItem(int bookID, String title, String author) {
        this.bookID = bookID;
        this.title = title;
        this.author = author;
        this.bookmark = "";
    }

    /**
     * Instantiates a new Book item.
     *
     * @param bookID the book id
     * @param title  the title
     * @param author the author
     * @param genre  the genre
     */
    public BookItem(int bookID, String title, String author, String genre) {
        this(bookID, title, author);
        this.genre = genre;
    }

    /**
     * Instantiates a new Book item.
     *
     * @param bookID the book id
     * @param title  the title
     * @param author the author
     * @param genre  the genre
     * @param imgURL the img url
     */
    public BookItem(int bookID, String title, String author, String genre, String imgURL) {
        this(bookID, title, author, genre);
        this.imgURL = imgURL;
    }

    /**
     * Gets book id.
     *
     * @return the book id
     */
    public int getBookID() {
        return bookID;
    }

    /**
     * Sets book id.
     *
     * @param bookID the book id
     */
    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    /**
     * Gets image id.
     *
     * @return the image id
     */
    public int getImageID() {
        return imageID;
    }

    /**
     * Sets image id.
     *
     * @param imageID the image id
     */
    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    /**
     * Gets bookmark.
     *
     * @return the bookmark
     */
    public String getBookmark() {
        return bookmark;
    }

    /**
     * Sets bookmark.
     *
     * @param bookmark the bookmark
     */
    public void setBookmark(String bookmark) {
        this.bookmark = bookmark;
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets author.
     *
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets author.
     *
     * @param author the author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Gets genre.
     *
     * @return the genre
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Sets genre.
     *
     * @param genre the genre
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * Gets img url.
     *
     * @return the img url
     */
    public String getImgURL() {
        return imgURL;
    }

    /**
     * Sets img url.
     *
     * @param imgURL the img url
     */
    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    @Override
    public String toString() {
        return "ID: " + bookID + " Title: " + title;
    }

    /**
     * Compares this book item to given book item by ID.
     *
     * @param item the book that is compared
     */
    @Override
    public <T> boolean isSameModelAs(@NonNull T item) {
        if (item instanceof BookItem) {
            final BookItem other = (BookItem) item;
            return other.bookID == this.bookID;
        }
        return false;
    }

    /**
     * Compares this book item to given book item by content (title).
     *
     * @param item the book that is compared
     */
    @Override
    public <T> boolean isContentTheSameAs(@NonNull T item) {
        if (item instanceof BookItem) {
            final BookItem other = (BookItem) item;
            return this.title != null ? this.title.equals(other.getTitle()) : other.getTitle() == null;
        }
        return false;
    }
}
