package fi.mkauha.bookshelf.models;

/**
 * Library POJO-class.
 *
 * @author  Miko Kauhanen
 * @version 1.0
 */
public class Library {
    private long id;
    private String name;
    private String consortium;
    private String address;
    private String zip;
    private boolean isOpen;
    private String openHours;
    private double latitude;
    private double longitude;
    private boolean isMainLibrary;

    /**
     * Instantiates a new Library.
     */
    public Library() {
    }

    /**
     * Instantiates a new Library.
     *
     * @param id            the id
     * @param name          the name
     * @param consortium    the consortium
     * @param address       the address
     * @param zip           the zip
     * @param isOpen        the is open
     * @param openHours     the open hours
     * @param latitude      the latitude
     * @param longitude     the longitude
     * @param isMainLibrary the is main library
     */
    public Library(long id, String name, String consortium, String address, String zip, boolean isOpen, String openHours, double latitude, double longitude, boolean isMainLibrary) {
        this.id = id;
        this.name = name;
        this.consortium = consortium;
        this.address = address;
        this.zip = zip;
        this.isOpen = isOpen;
        this.openHours = openHours;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isMainLibrary = isMainLibrary;
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

    /**
     * Gets consortium.
     *
     * @return the consortium
     */
    public String getConsortium() {
        return consortium;
    }

    /**
     * Sets consortium.
     *
     * @param consortium the consortium
     */
    public void setConsortium(String consortium) {
        this.consortium = consortium;
    }

    /**
     * Gets address.
     *
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets address.
     *
     * @param address the address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets zip.
     *
     * @return the zip
     */
    public String getZip() {
        return zip;
    }

    /**
     * Sets zip.
     *
     * @param zip the zip
     */
    public void setZip(String zip) {
        this.zip = zip;
    }

    /**
     * Is open boolean.
     *
     * @return the boolean
     */
    public boolean isOpen() {
        return isOpen;
    }

    /**
     * Sets open.
     *
     * @param open the open
     */
    public void setOpen(boolean open) {
        isOpen = open;
    }

    /**
     * Gets open hours.
     *
     * @return the open hours
     */
    public String getOpenHours() {
        return openHours;
    }

    /**
     * Sets open hours.
     *
     * @param openHours the open hours
     */
    public void setOpenHours(String openHours) {
        this.openHours = openHours;
    }

    /**
     * Gets latitude.
     *
     * @return the latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Sets latitude.
     *
     * @param latitude the latitude
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Gets longitude.
     *
     * @return the longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Sets longitude.
     *
     * @param longitude the longitude
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Is main library boolean.
     *
     * @return the boolean
     */
    public boolean isMainLibrary() {
        return isMainLibrary;
    }

    /**
     * Sets main library.
     *
     * @param mainLibrary the main library
     */
    public void setMainLibrary(boolean mainLibrary) {
        isMainLibrary = mainLibrary;
    }

    @Override
    public String toString() {
        return "Library{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", consortium='" + consortium + '\'' +
                ", address='" + address + '\'' +
                ", zip='" + zip + '\'' +
                ", isOpen=" + isOpen +
                ", openHours='" + openHours + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", isMainLibrary=" + isMainLibrary +
                '}';
    }
}
