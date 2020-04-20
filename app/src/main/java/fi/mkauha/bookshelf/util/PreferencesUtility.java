package fi.mkauha.bookshelf.util;


import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import fi.mkauha.bookshelf.models.BookItem;

public class PreferencesUtility {

    private SharedPreferences prefs;
    private Gson gson;

    public PreferencesUtility(SharedPreferences prefs) {
        this.prefs = prefs;
        gson = new Gson();
    }

    public void putOne(String key, BookItem bookItem) {
        ArrayList<BookItem> bookItems = getAll(key);
        bookItems.add(bookItem);
        putAll(key, bookItems);
    }

    public void putAll(String key, Collection<BookItem> bookItems) {
        SharedPreferences.Editor editor = prefs.edit();
        try {
            String values = gson.toJson(bookItems);
            editor.putString(key,  values);
        } catch (Exception e) {
            e.printStackTrace();
        }
        editor.apply();
    }

    public BookItem getOne(String key, int id) {
        BookItem rBook = null;
        ArrayList<BookItem> bookItems = getAll(key);
        for(BookItem book : bookItems) {
            if(book.getBookID() == id) {
                rBook = book;
                break;
            }
        }
        return rBook;
    }

    public ArrayList<BookItem> getAll(String key) {
        ArrayList<BookItem> list = new ArrayList<>();
        String json;
        Type listType = new TypeToken<ArrayList<BookItem>>(){}.getType();

        if(prefs.contains(key)) {
            json = prefs.getString(key, "");
            list = gson.fromJson(json, listType);
        }
        return list;
    }

    public void updateOne(String key, BookItem updatedBookItem) {
        ArrayList<BookItem> bookItems = getAll(key);

        int i = 0;
        for(BookItem book : bookItems) {
            if(book.getBookID() == updatedBookItem.getBookID()) {
                bookItems.set(i, updatedBookItem);
                putAll(key, bookItems);
                break;
            }
            i++;
        }
    }

    public void removeOne(String key, int id) {
        ArrayList<BookItem> bookItems = getAll(key);

        int i = 0;
        for(BookItem book : bookItems) {
            if (book.getBookID() == id) {
                bookItems.remove(book);
                putAll(key, bookItems);
                break;
            }
            i++;
        }
    }
}
