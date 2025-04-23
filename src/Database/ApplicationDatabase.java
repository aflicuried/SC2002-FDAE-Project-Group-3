package Database;

import Entity.Application;
import Entity.Project;
import Entity.User;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ApplicationDatabase {
    private static final ApplicationDatabase instance = new ApplicationDatabase();
    private List<Application> applications = new ArrayList<>();
    private static final String FILE_PATH = "data/ApplicationList.csv";

    private ApplicationDatabase() {}

    public static ApplicationDatabase getInstance() {
        return instance;
    }

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
                String[] parts = line.split(",", -1);//keep empty field
                if (parts.length >= 5) {
                    String userNric = parts[0];
                    String projectName = parts[1];
                    String statusStr = parts[2];
                    String flatTypeStr = parts[3];
                    boolean withdrawal = Boolean.parseBoolean(parts[4]);

                    // get User and Project
                    User user = UserDatabase.getInstance().findByNric(userNric);
                    Project project = ProjectDatabase.getInstance().findProjectByName(projectName);

                    Application.ApplicationStatus status;
                    try {
                        status = Application.ApplicationStatus.valueOf(statusStr);
                    } catch (IllegalArgumentException e) {
                        continue; // skip none status
                    }

                    Application.FlatType flatType;
                    try {
                        flatType = Application.FlatType.valueOf(flatTypeStr);
                    } catch (IllegalArgumentException e) {
                        continue; // skip none flat type
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

    // write applications.csv
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

    // add Application
    public void addApplication(Application application) {
        applications.add(application);
    }

    // get Applications copies
    public List<Application> findApplications() {
        return applications;
    }

    // find Application by Project
    public List<Application> findByProject(Project project) {
        return applications.stream()
                .filter(a -> a.getProject().equals(project))
                .collect(Collectors.toList());
    }

    // find Application by Nric
    public Application findByApplicantNric(String nric) {
        return applications.stream()
                .filter(a -> a.getUser().getNric().equals(nric))
                .findFirst().orElse(null);
    }

    // find Application by Withdrawal
    public List<Application> findByWithdrawal(boolean withdrawal) {
        return applications.stream()
                .filter(a -> a.isWithdrawal() == withdrawal)
                .collect(Collectors.toList());
    }

    /* find to extend
    public List<Application> findBy(Predicate<Application> predicate) {
        return applications.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }*/
}