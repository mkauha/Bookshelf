package fi.mkauha.bookshelf.util;


import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import fi.mkauha.bookshelf.models.BookItem;

/**
 * Helper class for saving and retrieving book item data from SharedPreferences.
 *
 * @author  Miko Kauhanen
 * @version 1.0
 */
public class PreferencesUtility {

    private SharedPreferences prefs;
    private Gson gson;

    /**
     * Instantiates a new Preferences utility.
     *
     * @param prefs the SharedPreferences
     */
    public PreferencesUtility(SharedPreferences prefs) {
        this.prefs = prefs;
        gson = new Gson();
    }

    /**
     * Put one book item to SharedPreferences.
     *
     * Adds a book item to collection and calls method that adds that collection to SharedPreferences.
     *
     * @param key      the key to save under
     * @param bookItem the book item
     */
    public void putOne(String key, BookItem bookItem) {
        ArrayList<BookItem> bookItems = getAll(key);
        bookItems.add(bookItem);
        putAll(key, bookItems);
    }

    /**
     * Put multiple book items to SharedPreferences.
     *
     * Converts collection to JSON string and puts it to SharedPreferences.
     *
     * @param key       the key to save under
     * @param bookItems the book items
     */
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

    /**
     * Get one book item from SharedPreferences.
     *
     * Gets one book item from SharedPreferences by incrementing over the book item list until book with same ID is found.
     *
     * @param key the key to get values from
     * @param id  the book item id
     * @return the book that has the correct id
     */
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

    /**
     * Get all book item from SharedPreferences.
     *
     * Gets all book items from SharedPreferences and converts it to a list.
     *
     * @param key the key to get values from
     * @return the books items in a list
     */
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

    /**
     * Updates one book item.
     *
     * Increment over the book items until book with correct ID is found then replacing that with given book item.
     *
     * @param key             the key where values are updated
     * @param updatedBookItem the updated book item
     */
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

    /**
     * Remove one book item.
     *
     * Increment over the book items until book with correct ID is found then removing it.
     *
     * @param key the key where value is removed
     * @param id  the book item id that is removed.
     */
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
