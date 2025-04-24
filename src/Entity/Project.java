package Entity;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents a public housing project managed by the HDB.
 * Contains information about the project's name, location, unit types, availability, timeline, and assigned officers.
 */
public class Project {
    private boolean visibility;
    private String name;
    private String neighbourhood;
    private List<FlatType> flatTypes;
    private LocalDate openingDate;
    private LocalDate closingDate;
    private HDBManager manager;
    private int officerSlots;
    private List<HDBOfficer> officers;

    /**
     * Constructs a new Project instance with the provided parameters.
     *
     * @param name           the project name
     * @param neighbourhood  the location of the project
     * @param flatTypes      list of flat types and their details
     * @param openingDate    the opening date of the project
     * @param closingDate    the closing date of the project
     * @param manager        the HDB manager in charge
     * @param officerSlots   number of officer slots available
     * @param officers       list of officers assigned to the project
     */
    public Project(String name, String neighbourhood, List<FlatType> flatTypes, LocalDate openingDate, LocalDate closingDate, HDBManager manager,
                   int officerSlots, List<HDBOfficer> officers) {
        this.name = name;
        this.neighbourhood = neighbourhood;
        this.flatTypes = flatTypes;
        this.openingDate = openingDate;
        this.closingDate = closingDate;
        this.manager = manager;
        this.officerSlots = officerSlots;
        this.officers = officers;
    }

    /**
     * Checks whether the project is visible to users.
     *
     * @return true if visible, false otherwise
     */
    public boolean isVisible() { return visibility; }

    /**
     * Returns the project name.
     *
     * @return the name of the project
     */
    public String getName() { return this.name; }

    /**
     * Returns the neighbourhood of the project.
     *
     * @return the neighbourhood location
     */
    public String getNeighbourhood() { return neighbourhood; }

    /**
     * Gets the number of available 2-Room units.
     *
     * @return number of 2-Room units
     */
    public int get2RoomUnits() {
        return this.flatTypes.stream()
                .filter(r -> r.getType().equals("2-Room")).findFirst().get()
                .getAvailableUnits();
    }

    /**
     * Gets the number of available 3-Room units.
     *
     * @return number of 3-Room units
     */
    public int get3RoomUnits() {
        return this.flatTypes.stream()
                .filter(r -> r.getType().equals("3-Room")).findFirst().get()
                .getAvailableUnits();
    }

    /**
     * Gets the price for a 2-Room flat.
     *
     * @return price of a 2-Room unit
     */
    public int get2RoomPrice() {
        return this.flatTypes.stream()
                .filter(r -> r.getType().equals("2-Room")).findFirst().get()
                .getPrice();
    }

    /**
     * Gets the price for a 3-Room flat.
     *
     * @return price of a 3-Room unit
     */
    public int get3RoomPrice() {
        return this.flatTypes.stream()
                .filter(r -> r.getType().equals("3-Room")).findFirst().get()
                .getPrice();
    }

    /**
     * Returns the opening date of the project.
     *
     * @return the opening date
     */
    public LocalDate getOpeningDate() { return openingDate; }

    /**
     * Returns the closing date of the project.
     *
     * @return the closing date
     */
    public LocalDate getClosingDate() { return closingDate; }

    /**
     * Gets the HDB manager assigned to the project.
     *
     * @return the project manager
     */
    public HDBManager getManager() { return manager; }

    /**
     * Returns the number of officer slots available.
     *
     * @return number of officer slots
     */
    public int getOfficerSlots() { return officerSlots; }

    /**
     * Returns the list of officers assigned to the project.
     *
     * @return list of HDB officers
     */
    public List<HDBOfficer> getOfficers() { return officers; }

    /**
     * Sets the project name.
     *
     * @param name the new project name
     */
    public void setName(String name) { this.name = name; }

    /**
     * Sets the neighbourhood of the project.
     *
     * @param neighbourhood the new neighbourhood
     */
    public void setNeighbourhood(String neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    /**
     * Sets the number of available 2-Room units.
     *
     * @param availableUnits number of 2-Room units
     */
    public void set2RoomUnits(int availableUnits) {
        this.flatTypes.stream()
                .filter(r -> r.getType().equals("2-Room")).findFirst().get()
                .setAvailableUnits(availableUnits);
    }

    /**
     * Sets the number of available 3-Room units.
     *
     * @param availableUnits number of 3-Room units
     */
    public void set3RoomUnits(int availableUnits) {
        this.flatTypes.stream()
                .filter(r -> r.getType().equals("3-Room")).findFirst().get()
                .setAvailableUnits(availableUnits);
    }

    /**
     * Sets the price of a 2-Room unit.
     *
     * @param price price for a 2-Room unit
     */
    public void set2RoomPrice(int price) {
        this.flatTypes.stream()
                .filter(r -> r.getType().equals("2-Room")).findFirst().get()
                .setPrice(price);
    }

    /**
     * Sets the price of a 3-Room unit.
     *
     * @param price price for a 3-Room unit
     */
    public void set3RoomPrice(int price) {
        this.flatTypes.stream()
                .filter(r -> r.getType().equals("3-Room")).findFirst().get()
                .setPrice(price);
    }

    /**
     * Sets the opening date of the project.
     *
     * @param openingDate the new opening date
     */
    public void setOpeningDate(LocalDate openingDate) { this.openingDate = openingDate; }

    /**
     * Sets the closing date of the project.
     *
     * @param closingDate the new closing date
     */
    public void setClosingDate(LocalDate closingDate) { this.closingDate = closingDate; }

    /**
     * Sets the number of officer slots.
     *
     * @param officerSlots the new officer slot count
     */
    public void setOfficerSlots(int officerSlots) { this.officerSlots = officerSlots; }

    /**
     * Sets the visibility of the project.
     *
     * @param visibility true to make visible, false to hide
     */
    public void setVisibility(boolean visibility) { this.visibility = visibility; }
}
