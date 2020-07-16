package fi.mkauha.bookshelf.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Book implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    private String isbn;

    private String title;

    private String author;

    private String genres;

    private String year;

    private String pages;

    private String image;

    private String summary;

    private String languages;

    private String collection;

    private int bookmark;

    public Book(String isbn, String title, String author,
                String genres, String year, String pages, String image,
                String summary, String languages, String collection, int bookmark) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.genres = genres;
        this.year = year;
        this.pages = pages;
        this.image = image;
        this.summary = summary;
        this.languages = languages;
        this.collection = collection;
        this.bookmark = bookmark;
    }

    protected Book(Parcel in) {
        uid = in.readInt();
        isbn = in.readString();
        title = in.readString();
        author = in.readString();
        genres = in.readString();
        year = in.readString();
        pages = in.readString();
        image = in.readString();
        summary = in.readString();
        languages = in.readString();
        collection = in.readString();
        bookmark = in.readInt();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
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

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public int getBookmark() {
        return bookmark;
    }

    public void setBookmark(int bookmark) {
        this.bookmark = bookmark;
    }

    public void update(String isbn, String title, String author,
                        String genres, String year, String pages, String image,
                        String summary, String languages, String collection, int bookmark) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.genres = genres;
        this.year = year;
        this.pages = pages;
        this.image = image;
        this.summary = summary;
        this.languages = languages;
        this.collection = collection;
        this.bookmark = bookmark;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(uid);
        parcel.writeString(isbn);
        parcel.writeString(title);
        parcel.writeString(author);
        parcel.writeString(genres);
        parcel.writeString(year);
        parcel.writeString(pages);
        parcel.writeString(image);
        parcel.writeString(summary);
        parcel.writeString(languages);
        parcel.writeString(collection);
        parcel.writeInt(bookmark);
    }
}

