package fi.mkauha.bookshelf.util;

import java.util.UUID;

public class UniqueIDGenerator {
    public static int generate() {
        UUID idOne = UUID.randomUUID();
        String str=""+idOne;
        int uid=str.hashCode();
        String filterStr=""+uid;
        str=filterStr.replaceAll("-", "");
        return Integer.parseInt(str);
    }
}
