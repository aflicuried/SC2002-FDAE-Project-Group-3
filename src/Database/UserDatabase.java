package Database;

import Entity.Applicant;
import Entity.HDBManager;
import Entity.HDBOfficer;
import Entity.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Singleton class that manages the loading, storing, and retrieval of user data,
 * including Applicants, HDB Officers, and HDB Managers.
 */
public class UserDatabase {
    private static final UserDatabase instance = new UserDatabase();
    private List<Applicant> applicants = new ArrayList<>();
    private List<HDBOfficer> officers = new ArrayList<>();
    private List<HDBManager> managers = new ArrayList<>();

    private UserDatabase() {}

    /**
     * Returns the singleton instance of UserDatabase.
     *
     * @return the singleton UserDatabase
     */
    public static UserDatabase getInstance() {
        return instance;
    }

    /**
     * Loads user data from CSV files and stores them in the respective lists.
     *
     * @throws IOException if an error occurs while reading the CSV files
     */
    public void loadData() throws IOException {
        applicants = readUser("data/ApplicantList.csv", User.UserRole.APPLICANT)
                .stream().map(u -> (Applicant) u).collect(Collectors.toList());

        officers = readUser("data/OfficerList.csv", User.UserRole.OFFICER)
                .stream().map(u -> (HDBOfficer) u).collect(Collectors.toList());

        managers = readUser("data/ManagerList.csv", User.UserRole.MANAGER)
                .stream().map(u -> (HDBManager) u).collect(Collectors.toList());
    }

    /**
     * Reads user data from a CSV file based on the specified role.
     *
     * @param filePath the path to the CSV file
     * @param role     the role of the users to read
     * @return a list of User objects of the given role
     * @throws IOException if an error occurs while reading the file
     */
    public List<User> readUser(String filePath, User.UserRole role) throws IOException {
        List<User> users = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filePath));

        String line;
        boolean isHeader = true;
        while ((line = br.readLine()) != null) {
            if (isHeader) {
                isHeader = false;
                continue;
            }
            String[] data = line.split(",");
            String name = data[0].trim();
            String nric = data[1].trim();
            int age = Integer.parseInt(data[2].trim());
            String status = data[3].trim().toUpperCase();
            String password = data[4].trim();

            User user;
            switch (role) {
                case APPLICANT -> user = new Applicant(name, nric, age, status, password);
                case OFFICER -> user = new HDBOfficer(name, nric, age, status, password);
                case MANAGER -> user = new HDBManager(name, nric, age, status, password);
                default -> throw new IllegalArgumentException("Invalid role");
            }
            users.add(user);
        }
        br.close();
        return users;
    }

    /**
     * Saves all current user data to the CSV file.
     *
     * @throws IOException if an I/O error occurs while writing the file
     */
    public void saveData() throws IOException {
        File directory = new File("data");
        if (!directory.exists()) {
            directory.mkdir();
        }

        saveApplicants();
        saveOfficers();
        saveManagers();
    }

    private void saveApplicants() throws IOException {
        File file = new File("data/ApplicantList.csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

            writer.write("Name,NRIC,Age,Marital Status,Password");
            writer.newLine();

            for (Applicant applicant : applicants) {
                if (applicant.getRole() == User.UserRole.APPLICANT) {
                    String line = String.format("%s,%s,%d,%s,%s",
                            applicant.getName(),
                            applicant.getNric(),
                            applicant.getAge(),
                            applicant.getMaritalStatus(),
                            applicant.getPassword());
                    writer.write(line);
                    writer.newLine();
                }
            }
        }
    }

    private void saveOfficers() throws IOException {
        File file = new File("data/OfficerList.csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

            writer.write("Name,NRIC,Age,Marital Status,Password");
            writer.newLine();

            for (HDBOfficer officer : officers) {
                String line = String.format("%s,%s,%d,%s,%s",
                        officer.getName(),
                        officer.getNric(),
                        officer.getAge(),
                        officer.getMaritalStatus(),
                        officer.getPassword());
                writer.write(line);
                writer.newLine();
            }
        }
    }

    private void saveManagers() throws IOException {
        File file = new File("data/ManagerList.csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

            writer.write("Name,NRIC,Age,Marital Status,Password");
            writer.newLine();

            for (HDBManager manager : managers) {
                String line = String.format("%s,%s,%d,%s,%s",
                        manager.getName(),
                        manager.getNric(),
                        manager.getAge(),
                        manager.getMaritalStatus(),
                        manager.getPassword());
                writer.write(line);
                writer.newLine();
            }
        }
    }

    /**
     * Returns a list of all applicants.
     *
     * @return list of Applicant objects
     */
    public List<Applicant> findApplicants() {
        return applicants;
    }

    /**
     * Returns a list of all HDB officers.
     *
     * @return list of HDBOfficer objects
     */
    public List<HDBOfficer> findOfficers() {
        return officers;
    }

    /**
     * Returns a list of all HDB managers.
     *
     * @return list of HDBManager objects
     */
    public List<HDBManager> findManagers() {
        return managers;
    }

    /**
     * Returns a list of all users regardless of role.
     *
     * @return list of all User objects
     */
    public List<User> findUsers() {
        return Stream.of(findApplicants(), findOfficers(), findManagers())
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    /**
     * Finds a user by their NRIC.
     *
     * @param nric the NRIC of the user
     * @return the matching User object, or null if not found
     */
    public User findByNric(String nric) {
        return findUsers().stream()
                .filter(user -> user.getNric().equals(nric))
                .findFirst().orElse(null);
    }
}
