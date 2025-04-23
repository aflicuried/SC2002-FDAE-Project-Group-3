package Database;

import Entity.HDBOfficer;
import Entity.Project;
import Entity.Registration;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Singleton class for managing registration records between HDB Officers and Projects.
 * Handles loading from and saving to a CSV file.
 */
public class RegistrationDatabase {
    private static final RegistrationDatabase instance = new RegistrationDatabase();
    private List<Registration> registrations = new ArrayList<>();
    private static int nextId = 1000;
    private static final String FILE_PATH = "data/RegistrationList.csv";

    private RegistrationDatabase() {}

    /**
     * Returns the singleton instance of the RegistrationDatabase.
     *
     * @return the RegistrationDatabase instance
     */
    public static RegistrationDatabase getInstance() {
        return instance;
    }

    /**
     * Loads registration data from the CSV file into memory.
     *
     * @throws IOException if an I/O error occurs while reading the file
     */
    public void loadData() throws IOException {
        registrations.clear();
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isHeader = true;
            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] parts = line.split(",", -1);
                if (parts.length >= 5) {
                    int id = Integer.parseInt(parts[0]);
                    String officerNric = parts[1];
                    String officerName = parts[2];
                    String projectName = parts[3];
                    String statusStr = parts[4];

                    HDBOfficer officer = (HDBOfficer) UserDatabase.getInstance().findByNric(officerNric);
                    Project project = ProjectDatabase.getInstance().findProjectByName(projectName);
                    Registration.Status status;

                    try {
                        status = Registration.Status.valueOf(statusStr);
                    } catch (IllegalArgumentException e) {
                        continue; // Skip invalid enum value
                    }

                    if (officer != null && project != null && officer.getName().equals(officerName)) {
                        Registration registration = new Registration(officer, project, status);
                        registration.setId(id);
                        registrations.add(registration);
                        nextId = Math.max(nextId, id + 1);
                    }
                }
            }
        }
    }

    /**
     * Saves all current registration records to the CSV file.
     *
     * @throws IOException if an I/O error occurs while writing the file
     */
    public void saveData() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write("ID,Officer NRIC,Officer Name,Project Name,Status");
            writer.newLine();

            for (Registration registration : registrations) {
                String line = String.format("%d,%s,%s,%s,%s",
                        registration.getId(),
                        registration.getOfficer().getNric(),
                        registration.getOfficer().getName(),
                        registration.getProject().getName(),
                        registration.getStatus().name());
                writer.write(line);
                writer.newLine();
            }
        }
    }

    /**
     * Returns all registrations in memory.
     *
     * @return list of Registration objects
     */
    public List<Registration> findAll() {
        return registrations;
    }

    /**
     * Adds a new registration to the database and assigns a unique ID.
     *
     * @param registration the Registration object to add
     */
    public void addRegistration(Registration registration) {
        registration.setId(nextId++);
        registrations.add(registration);
    }

    /**
     * Finds all registrations associated with a specific HDB officer's NRIC.
     *
     * @param nric the NRIC of the officer
     * @return list of matching Registration objects
     */
    public List<Registration> findByOfficerNric(String nric) {
        return registrations.stream()
                .filter(r -> r.getOfficer().getNric().equals(nric))
                .collect(Collectors.toList());
    }

    /**
     * Finds a registration by its unique ID.
     *
     * @param id the ID of the registration
     * @return the matching Registration object, or null if not found
     */
    public Registration findById(int id) {
        return registrations.stream()
                .filter(r -> r.getId() == id)
                .findFirst().orElse(null);
    }
}
