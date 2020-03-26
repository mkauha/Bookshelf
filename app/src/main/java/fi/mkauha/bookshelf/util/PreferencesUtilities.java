package fi.mkauha.bookshelf.util;


import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import fi.mkauha.bookshelf.items.BookItem;

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
        editor.clear();
        try {
            String values = gson.toJson(bookItems);
            editor.putString(key,  values);
            Log.d("PreferencesUtilities", "putAll key: " + key + " value: " + bookItems);
        } catch (Exception e) {
            e.printStackTrace();
        }
        editor.apply();
    }

    public ArrayList<BookItem> getAll(String key) {
        ArrayList<BookItem> list = new ArrayList<>();
        String json = "";
        Type listType = new TypeToken<ArrayList<BookItem>>(){}.getType();
        Log.d("PreferencesUtilities", "getAll prefs: " + prefs.getAll().toString());

        if(prefs.contains(key)) {
            json = prefs.getString(key, "");
            list = gson.fromJson(json, listType);
            //bookItem = gson.fromJson(json, BookItem.class);
            Log.d("PreferencesUtilities", "getAll key: " + key + " value: " + json);
        }
        return list;
    }
}
