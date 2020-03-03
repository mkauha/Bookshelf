package fi.mkauha.bookshelf.items;

public class BookItem {
    private int bookID;
    private String name;
    private String author;
    private String genre;
    private String tag;

    public BookItem(int bookID, String name, String author, String genre, String tag) {
        this.bookID = bookID;
        this.name = name;
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
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return name;
    }
}
