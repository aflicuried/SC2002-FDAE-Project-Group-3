package Database;

import Entity.Applicant;
import Entity.Enquiry;
import Entity.Project;
import Entity.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Singleton class that manages the storage, retrieval, and persistence of Enquiry objects.
 * Enquiries are stored in memory and saved to/loaded from a CSV file.
 */
public class EnquiryDatabase {
    private static final EnquiryDatabase instance = new EnquiryDatabase();
    private List<Enquiry> enquiries = new ArrayList<>();
    private static int nextId = 10000;
    private static final String FILE_PATH = "data/EnquiryList.csv";

    
    private EnquiryDatabase() {}

    /**
     * Returns the singleton instance of EnquiryDatabase.
     *
     * @return the single instance of EnquiryDatabase
     */
    public static EnquiryDatabase getInstance() {
        return instance;
    }

    /**
     * Loads enquiries from a CSV file into memory.
     * Skips entries where user or project cannot be found.
     * Updates the next available ID to avoid duplicates.
     *
     * @throws IOException if an I/O error occurs while reading the file
     */
    public void loadData() throws IOException {
        enquiries.clear();
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
                    String userNric = parts[1];
                    String projectName = parts[2];
                    String message = parts[3];
                    String response = parts[4].isEmpty() ? null : parts[4];

                    User user = UserDatabase.getInstance().findByNric(userNric);
                    Project project = ProjectDatabase.getInstance().findProjectByName(projectName);

                    if (user != null && project != null) {
                        Enquiry enquiry = new Enquiry(message, user, project);
                        enquiry.setId(id); // Set ID directly
                        if (response != null) {
                            enquiry.reply(response);
                        }
                        enquiries.add(enquiry);
                        nextId = Math.max(nextId, id + 1); // Ensure unique IDs
                    }
                }
            }
        }
    }

    /**
     * Saves the current list of enquiries to a CSV file.
     *
     * @throws IOException if an I/O error occurs while writing the file
     */
    public void saveData() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write("ID,User NRIC,Project Name,Message,Response");
            writer.newLine();

            for (Enquiry enquiry : enquiries) {
                String line = String.format("%d,%s,%s,%s,%s",
                        enquiry.getId(),
                        enquiry.getUser().getNric(),
                        enquiry.getProject().getName(),
                        enquiry.getMessage(),
                        enquiry.getResponse() != null ? enquiry.getResponse() : "");
                writer.write(line);
                writer.newLine();
            }
        }
    }

    /**
     * Adds a new enquiry to the list and assigns it a unique ID.
     *
     * @param enquiry the enquiry to be added
     */
    public void addEnquiry(Enquiry enquiry) {
        enquiry.setId(nextId++); // Set ID and increment
        enquiries.add(enquiry);
    }

    /**
     * Removes an enquiry from the list.
     *
     * @param enquiry the enquiry to remove
     */
    public void removeEnquiry(Enquiry enquiry) {
        enquiries.remove(enquiry);
    }

    /**
     * Returns all enquiries currently stored in memory.
     *
     * @return list of all enquiries
     */
    public List<Enquiry> findAll() {
        return enquiries;
    }

    /**
     * Finds an enquiry by its ID.
     *
     * @param id the ID of the enquiry
     * @return the matching enquiry, or null if not found
     */
    public Enquiry findById(int id) {
        return enquiries.stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Finds enquiries submitted by a user with the given name.
     *
     * @param name the name of the user
     * @return list of enquiries by the user
     */
    public List<Enquiry> findByName(String name) {
        return enquiries.stream()
                .filter(e -> e.getUser().getName().equals(name))
                .collect(Collectors.toList());
    }

    /**
     * Finds enquiries related to a specific project.
     *
     * @param project the project to search enquiries for
     * @return list of enquiries for the given project
     */
    public List<Enquiry> findByProject(Project project) {
        return enquiries.stream()
                .filter(e -> e.getProject().equals(project))
                .collect(Collectors.toList());
    }
}
