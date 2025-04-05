package Entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Project {
    private boolean visibility;
    private String name;
    private String neighbourhood;
    private List<FlatType> flatTypes;
    private LocalDate openingDate;
    private LocalDate closingDate; //Available from Java 8, to store date-format data
    private HDBManager manager;
    private int officerSlots;
    private List<HDBOfficer> officers;

    private List<Application> applications = new ArrayList<>();
    private List<Enquiry> enquiries = new ArrayList<>();


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

    //getters
    public boolean isVisible() { return visibility; }
    public String getName() { return this.name; }
    public String getNeighbourhood() { return neighbourhood; }

    public int get2RoomUnits() {
        return this.flatTypes.stream()
                .filter(r -> r.getType().equals("2-Room")).findFirst().get()
                .getAvailableUnits();
    }
    public int get3RoomUnits() {
        return this.flatTypes.stream()
                .filter(r -> r.getType().equals("3-Room")).findFirst().get()
                .getAvailableUnits();
    }
    public int get2RoomPrice() {
        return this.flatTypes.stream()
                .filter(r -> r.getType().equals("2-Room")).findFirst().get()
                .getPrice();
    }
    public int get3RoomPrice() {
        return this.flatTypes.stream()
                .filter(r -> r.getType().equals("3-Room")).findFirst().get()
                .getPrice();
    }

    public LocalDate getOpeningDate() { return openingDate; }
    public LocalDate getClosingDate() { return closingDate; }
    public HDBManager getManager() { return manager; }
    public int getOfficerSlots() { return officerSlots; }
    public List<HDBOfficer> getOfficers() { return officers; }
    public List<Application> getApplications() { return applications; }
    public List<Enquiry> getEnquiries() { return enquiries; }

    public void shiftVisibility() { this.visibility = !(this.visibility); }
}
