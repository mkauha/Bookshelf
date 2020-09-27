
package fi.mkauha.bookshelf.data.remote.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Record {

    @SerializedName("buildings")
    @Expose
    private List<Building> buildings = null;

    @SerializedName("formats")
    @Expose
    private List<Format> formats = null;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("images")
    @Expose
    private List<Object> images = null;

    @SerializedName("languages")
    @Expose
    private List<String> languages = null;

    @SerializedName("nonPresenterAuthors")
    @Expose
    private List<NonPresenterAuthor> nonPresenterAuthors = null;

    @SerializedName("onlineUrls")
    @Expose
    private List<OnlineUrl> onlineUrls = null;

    @SerializedName("presenters")
    @Expose
    private Presenters presenters;

    @SerializedName("rating")
    @Expose
    private Rating rating;

    @SerializedName("series")
    @Expose
    private List<Object> series = null;

    @SerializedName("subjects")
    @Expose
    private List<List<String>> subjects = null;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("urls")
    @Expose
    private List<Url> urls = null;

    @SerializedName("cleanIsbn")
    @Expose
    private String cleanIsbn = null;

    @SerializedName("authors")
    @Expose
    private List<Object> authors = null;

    @SerializedName("genres")
    @Expose
    private List<String> genres = null;

    @SerializedName("year")
    @Expose
    private String year = null;

    @SerializedName("summary")
    @Expose
    private List<String> summary = null;

    @SerializedName("physicalDescriptions")
    @Expose
    private List<String> physicalDescriptions = null;

    public List<Building> getBuildings() {
        return buildings;
    }

    public void setBuildings(List<Building> buildings) {
        this.buildings = buildings;
    }

    public List<Format> getFormats() {
        return formats;
    }

    public void setFormats(List<Format> formats) {
        this.formats = formats;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Object> getImages() {
        return images;
    }

    public void setImages(List<Object> images) {
        this.images = images;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public List<NonPresenterAuthor> getNonPresenterAuthors() {
        return nonPresenterAuthors;
    }

    public void setNonPresenterAuthors(List<NonPresenterAuthor> nonPresenterAuthors) {
        this.nonPresenterAuthors = nonPresenterAuthors;
    }

    public List<OnlineUrl> getOnlineUrls() {
        return onlineUrls;
    }

    public void setOnlineUrls(List<OnlineUrl> onlineUrls) {
        this.onlineUrls = onlineUrls;
    }

    public Presenters getPresenters() {
        return presenters;
    }

    public void setPresenters(Presenters presenters) {
        this.presenters = presenters;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public List<Object> getSeries() {
        return series;
    }

    public void setSeries(List<Object> series) {
        this.series = series;
    }

    public List<List<String>> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<List<String>> subjects) {
        this.subjects = subjects;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Url> getUrls() {
        return urls;
    }

    public void setUrls(List<Url> urls) {
        this.urls = urls;
    }

    public String getCleanIsbn() {
        return cleanIsbn;
    }

    public void setCleanIsbn(String cleanIsbn) {
        this.cleanIsbn = cleanIsbn;
    }

    public List<Object> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Object> authors) {
        this.authors = authors;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public List<String> getSummary() {
        return summary;
    }

    public void setSummary(List<String> summary) {
        this.summary = summary;
    }

    public void setPhysicalDescriptions(List<String> physicalDescriptions) {
        this.physicalDescriptions = physicalDescriptions;
    }

    public List<String> getPhysicalDescriptions() {
        return physicalDescriptions;
    }
}
