package Entity;

/**
 * Represents a type of flat available in a housing project, including
 * the number of units available and the price.
 */
public class FlatType {
    private String type; // e.g., "2-Room", "3-Room"
    private int availableUnits;
    private int price;

    /**
     * Constructs a FlatType object with specified type, available units, and price.
     *
     * @param type the type of the flat (e.g., "2-Room", "3-Room")
     * @param availableUnits number of available units of this type
     * @param price price of the flat
     */
    public FlatType(String type, int availableUnits, int price) {
        this.type = type;
        this.availableUnits = availableUnits;
        this.price = price;
    }

    /**
     * Gets the type of the flat.
     *
     * @return the flat type
     */
    public String getType() {
        return this.type;
    }

    /**
     * Gets the number of available units of this flat type.
     *
     * @return the number of available units
     */
    public int getAvailableUnits() {
        return this.availableUnits;
    }

    /**
     * Gets the price of the flat.
     *
     * @return the price
     */
    public int getPrice() {
        return this.price;
    }

    /**
     * Sets the type of the flat.
     *
     * @param type the flat type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Sets the number of available units for this flat type.
     *
     * @param availableUnits the number of units available
     */
    public void setAvailableUnits(int availableUnits) {
        this.availableUnits = availableUnits;
    }

    /**
     * Sets the price of the flat.
     *
     * @param price the price to set
     */
    public void setPrice(int price) {
        this.price = price;
    }
}
