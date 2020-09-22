package fi.mkauha.bookshelf.data.local.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity
public class Collection extends ArrayList<Book> {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    private String title;

    private int label;

    public Collection(String title, int label) {
        this.title = title;
        this.label = label;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }
}
