package fi.mkauha.bookshelf.util;


import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import fi.mkauha.bookshelf.model.BookItem;

public class PreferencesUtilities {

    private SharedPreferences prefs;
    private Gson gson;

    public PreferencesUtilities(SharedPreferences prefs) {
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
        //editor.clear();
        try {
            String values = gson.toJson(bookItems);
            editor.putString(key,  values);
            Log.d("PreferencesUtilities", "putAll key: " + key + " value: " + bookItems);
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
        Log.d("PreferencesUtilities", "getOne key: " + key + " " + id + ": " + rBook);
        return rBook;
    }

    public ArrayList<BookItem> getAll(String key) {
        ArrayList<BookItem> list = new ArrayList<>();
        String json;
        Type listType = new TypeToken<ArrayList<BookItem>>(){}.getType();
        //Log.d("PreferencesUtilities", "getAll prefs: " + prefs.getAll().toString());

        if(prefs.contains(key)) {
            json = prefs.getString(key, "");
            list = gson.fromJson(json, listType);
            Log.d("PreferencesUtilities", "getAll key: " + key + " value: " + json);
        }
        return list;
    }

    public boolean updateOne(String key, BookItem updatedBookItem) {
        boolean rBool = false;
        ArrayList<BookItem> bookItems = getAll(key);

        int i = 0;
        for(BookItem book : bookItems) {
            if(book.getBookID() == updatedBookItem.getBookID()) {
                bookItems.set(i, updatedBookItem);
                Log.d("PreferencesUtilities", "updateOne key: " + key + " value: " + updatedBookItem);
                putAll(key, bookItems);
                rBool = true;
                break;
            }
            i++;
        }
        return rBool;
    }
}
