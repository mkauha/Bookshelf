
package fi.mkauha.bookshelf.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Building {

    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("translated")
    @Expose
    private String translated;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTranslated() {
        return translated;
    }

    public void setTranslated(String translated) {
        this.translated = translated;
    }

}
