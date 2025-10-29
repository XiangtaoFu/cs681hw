package umbcs681.hw5;

public class BostonData {
    private String neighborhood;
    private String propertyType;
    private double price;
    private int bedrooms;
    private int bathrooms;
    private double squareFeet;
    private int yearBuilt;
    private String condition;
    private double latitude;
    private double longitude;
    
    public BostonData(String neighborhood, String propertyType, double price, 
                     int bedrooms, int bathrooms, double squareFeet, 
                     int yearBuilt, String condition, double latitude, double longitude) {
        this.neighborhood = neighborhood;
        this.propertyType = propertyType;
        this.price = price;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.squareFeet = squareFeet;
        this.yearBuilt = yearBuilt;
        this.condition = condition;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    // Getters
    public String getNeighborhood() { return neighborhood; }
    public String getPropertyType() { return propertyType; }
    public double getPrice() { return price; }
    public int getBedrooms() { return bedrooms; }
    public int getBathrooms() { return bathrooms; }
    public double getSquareFeet() { return squareFeet; }
    public int getYearBuilt() { return yearBuilt; }
    public String getCondition() { return condition; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    
    @Override
    public String toString() {
        return "BostonData{" +
                "neighborhood='" + neighborhood + '\'' +
                ", propertyType='" + propertyType + '\'' +
                ", price=" + price +
                ", bedrooms=" + bedrooms +
                ", bathrooms=" + bathrooms +
                ", squareFeet=" + squareFeet +
                ", yearBuilt=" + yearBuilt +
                ", condition='" + condition + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
} 