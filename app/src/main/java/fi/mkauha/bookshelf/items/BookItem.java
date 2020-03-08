package fi.mkauha.bookshelf.items;

import java.io.Serializable;

public class BookItem implements Serializable {
    private int bookID;
    private int imageID;
    private String title;
    private String author;
    private String genre;
    private String tag;

    public BookItem(int imageID, String title, String author) {
        //this.bookID = bookID;
        this.imageID = imageID;
        this.title = title;
        this.author = author;
    }
    public BookItem(int imageID, String title, String author, String genre) {
        this(imageID, title, author);
        this.genre = genre;
    }
    public BookItem(int imageID, String title, String author, String genre, String tag) {
        this(imageID, title, author, genre);
        this.tag = tag;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
