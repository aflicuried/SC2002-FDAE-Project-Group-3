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
}