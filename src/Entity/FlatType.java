package Entity;

public class FlatType {
    private String type; // "2-Room", "3-Room"
    private int availableUnits;
    private int price;

    public FlatType(String type, int availableUnits, int price) {
        this.type = type;
        this.availableUnits = availableUnits;
        this.price = price;
    }
     public String getType() {
        return this.type;
     }
     public int getAvailableUnits() {
        return this.availableUnits;
     }
     public int getPrice() {
        return this.price;
     }
     public void setType(String type) {
        this.type = type;
     }
     public void setAvailableUnits(int availableUnits) {
        this.availableUnits = availableUnits;
     }
     public void setPrice(int price) {
        this.price = price;
     }
}