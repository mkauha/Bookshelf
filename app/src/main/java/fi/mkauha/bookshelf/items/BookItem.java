package fi.mkauha.bookshelf.items;

import java.io.Serializable;

public class BookItem implements Serializable {
    private int bookID;
    private String title;
    private String author;
    private String genre;
    private String tag;

    public BookItem(int bookID, String name, String author, String genre, String tag) {
        this.bookID = bookID;
        this.title = name;
        this.author = author;
        this.genre = genre;
        this.tag = tag;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public String getName() {
        return title;
    }

    public void setName(String title) {
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return title;
    }
}
