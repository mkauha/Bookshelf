package fi.mkauha.bookshelf.data.remote.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Authors implements Serializable {

    @SerializedName("primary")
    @Expose
    private String primary;
    @SerializedName("secondary")
    @Expose
    private List<Object> secondary = null;
    @SerializedName("corporate")
    @Expose
    private List<Object> corporate = null;

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    public List<Object> getSecondary() {
        return secondary;
    }

    public void setSecondary(List<Object> secondary) {
        this.secondary = secondary;
    }

    public List<Object> getCorporate() {
        return corporate;
    }

    public void setCorporate(List<Object> corporate) {
        this.corporate = corporate;
    }


}
