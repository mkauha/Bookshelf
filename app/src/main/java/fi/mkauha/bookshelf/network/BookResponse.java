
package fi.mkauha.bookshelf.network;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookResponse {

    @SerializedName("resultCount")
    @Expose
    private Integer resultCount;
    @SerializedName("records")
    @Expose
    private List<Record> records = null;
    @SerializedName("status")
    @Expose
    private String status;

    public Integer getResultCount() {
        return resultCount;
    }

    public void setResultCount(Integer resultCount) {
        this.resultCount = resultCount;
    }

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "BookResponse{" +
                "resultCount=" + resultCount +
                ", records=" + records +
                ", status='" + status + '\'' +
                '}';
    }
}
