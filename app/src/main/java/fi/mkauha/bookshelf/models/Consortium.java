package fi.mkauha.bookshelf.models;

/**
 * Consortium POJO-class.
 *
 * @author  Miko Kauhanen
 * @version 1.0
 */
public class Consortium {
    private long id;
    private String name;
    private double latitude;
    private double longitude;

    /**
     * Instantiates a new Consortium.
     *
     * @param id   the id
     * @param name the name
     */
    public Consortium(long id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
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
