package Database;

import Entity.Application;
import Entity.Project;
import Entity.User;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Singleton class that manages the storage and retrieval of Application objects.
 * Applications are stored in memory and persisted to a CSV file.
 */
public class ApplicationDatabase {
    private static final ApplicationDatabase instance = new ApplicationDatabase();
    private List<Application> applications = new ArrayList<>();
    private static final String FILE_PATH = "data/ApplicationList.csv";

    private ApplicationDatabase() {}

    /**
     * Returns the singleton instance of ApplicationDatabase.
     *
     * @return the single instance of ApplicationDatabase
     */
    public static ApplicationDatabase getInstance() {
        return instance;
    }

    /**
     * Loads application data from a CSV file into memory.
     * Skips entries with invalid status or flat type, and skips if user or project not found.
     *
     * @throws IOException if there is an issue reading the file
     */
    public void loadData() throws IOException {
        applications.clear();
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
                String[] parts = line.split(",", -1); // keep empty fields
                if (parts.length >= 5) {
                    String userNric = parts[0];
                    String projectName = parts[1];
                    String statusStr = parts[2];
                    String flatTypeStr = parts[3];
                    boolean withdrawal = Boolean.parseBoolean(parts[4]);

                    User user = UserDatabase.getInstance().findByNric(userNric);
                    Project project = ProjectDatabase.getInstance().findProjectByName(projectName);

                    Application.ApplicationStatus status;
                    try {
                        status = Application.ApplicationStatus.valueOf(statusStr);
                    } catch (IllegalArgumentException e) {
                        continue; // skip invalid status
                    }

                    Application.FlatType flatType;
                    try {
                        flatType = Application.FlatType.valueOf(flatTypeStr);
                    } catch (IllegalArgumentException e) {
                        continue; // skip invalid flat type
                    }

                    if (user != null && project != null) {
                        Application application = new Application(user, project, status, flatType);
                        application.setWithdrawal(withdrawal);
                        applications.add(application);
                    }
                }
            }
        }
    }

    /**
     * Saves the in-memory application list to a CSV file.
     *
     * @throws IOException if there is an issue writing the file
     */
    public void saveData() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write("Applicant NRIC,Project Name,Status,Flat Type,Withdrawal");
            writer.newLine();

            for (Application application : applications) {
                String line = String.format("%s,%s,%s,%s,%b",
                        application.getUser().getNric(),
                        application.getProject().getName(),
                        application.getStatus().name(),
                        application.getFlatType().name(),
                        application.isWithdrawal());
                writer.write(line);
                writer.newLine();
            }
        }
    }

    /**
     * Adds a new application to the list.
     *
     * @param application the application to add
     */
    public void addApplication(Application application) {
        applications.add(application);
    }

    /**
     * Returns all applications.
     *
     * @return list of all applications
     */
    public List<Application> findApplications() {
        return applications;
    }

    /**
     * Finds applications associated with a specific project.
     *
     * @param project the project to filter by
     * @return list of applications for the given project
     */
    public List<Application> findByProject(Project project) {
        return applications.stream()
                .filter(a -> a.getProject().equals(project))
                .collect(Collectors.toList());
    }

    /**
     * Finds an application by the applicant's NRIC.
     *
     * @param nric the NRIC of the applicant
     * @return the application or null if not found
     */
    public Application findByApplicantNric(String nric) {
        return applications.stream()
                .filter(a -> a.getUser().getNric().equals(nric))
                .findFirst().orElse(null);
    }

    /**
     * Finds applications by withdrawal status.
     *
     * @param withdrawal true to find withdrawn applications, false otherwise
     * @return list of applications matching the withdrawal status
     */
    public List<Application> findByWithdrawal(boolean withdrawal) {
        return applications.stream()
                .filter(a -> a.isWithdrawal() == withdrawal)
                .collect(Collectors.toList());
    }

    /**
     * Flexible method to find applications using a predicate.
     * (Uncomment to use.)
     *
     * @param predicate a condition to filter applications
     * @return list of applications that match the predicate
     */
    /*
    public List<Application> findBy(Predicate<Application> predicate) {
        return applications.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }
    */
}
