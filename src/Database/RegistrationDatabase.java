package Database;

import Entity.HDBOfficer;
import Entity.Project;
import Entity.Registration;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RegistrationDatabase {
    private static final RegistrationDatabase instance = new RegistrationDatabase();
    private List<Registration> registrations = new ArrayList<>();
    private static int nextId = 1000; // init ID
    private static final String FILE_PATH = "data/RegistrationList.csv";

    private RegistrationDatabase() {}

    public static RegistrationDatabase getInstance() {
        return instance;
    }

    // read registrations.csv
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
                        continue;
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

    // write into registrations.csv
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

    public List<Registration> findAll() {
        return registrations;
    }

    // add registration
    public void addRegistration(Registration registration) {
        registration.setId(nextId++);
        registrations.add(registration);
    }

    // find by Officer NRIC
    public List<Registration> findByOfficerNric(String nric) {
        return registrations.stream()
                .filter(r -> r.getOfficer().getNric().equals(nric))
                .collect(Collectors.toList());
    }

    public Registration findById(int id) {
        return registrations.stream()
                .filter(r -> r.getId() == id)
                .findFirst().orElse(null);
    }
}