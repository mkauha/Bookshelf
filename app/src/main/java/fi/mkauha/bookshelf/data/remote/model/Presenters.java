
package fi.mkauha.bookshelf.data.remote.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Presenters {

    @SerializedName("presenters")
    @Expose
    private List<Object> presenters = null;
    @SerializedName("details")
    @Expose
    private List<Object> details = null;

    public List<Object> getPresenters() {
        return presenters;
    }

    public void setPresenters(List<Object> presenters) {
        this.presenters = presenters;
    }

    public List<Object> getDetails() {
        return details;
    }

    public void setDetails(List<Object> details) {
        this.details = details;
    }

}
