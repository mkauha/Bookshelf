package fi.mkauha.bookshelf.util;

import android.util.Log;

public final class IDGenerator {
    private static int id;

    private IDGenerator() {id = 0;};

    public static int generate() {
        id++;
        Log.d("idGenerator", "id: " + id);
        return id;
    }
}
