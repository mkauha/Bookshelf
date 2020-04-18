package fi.mkauha.bookshelf.models;

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

    public Library() {
    }

    public Library(long id, String name, String consortium, String address, String zip, boolean isOpen, String openHours, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.consortium = consortium;
        this.address = address;
        this.zip = zip;
        this.isOpen = isOpen;
        this.openHours = openHours;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public String getConsortium() {
        return consortium;
    }

    public void setConsortium(String consortium) {
        this.consortium = consortium;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public String getOpenHours() {
        return openHours;
    }

    public void setOpenHours(String openHours) {
        this.openHours = openHours;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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
                '}';
    }
}
