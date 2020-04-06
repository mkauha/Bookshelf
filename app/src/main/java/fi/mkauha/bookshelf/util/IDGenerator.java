package fi.mkauha.bookshelf.util;

import android.content.Context;
import android.util.Log;
import android.content.SharedPreferences;

public final class IDGenerator {
    private static int id;
    private static final String KEY = "ID";
    private static final String SHARED_PREFS = "bookshelf_preferences";

    private IDGenerator() {
        id = 0;
    }

    public static int generate(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        id = prefs.getInt(KEY, 0);
        id++;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY, id);
        editor.apply();
        Log.d("idGenerator", "id: " + id);
        return id;
    }
}
