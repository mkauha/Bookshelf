package fi.mkauha.bookshelf.models;

public class Consortium {
    private long id;
    private String name;
    private double latitude;
    private double longitude;

    public Consortium(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Consortium{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
