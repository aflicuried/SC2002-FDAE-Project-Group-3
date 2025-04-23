package Database;

import Entity.Applicant;
import Entity.Enquiry;
import Entity.Project;
import Entity.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EnquiryDatabase {
    private static final EnquiryDatabase instance = new EnquiryDatabase();
    private List<Enquiry> enquiries = new ArrayList<>();
    private static int nextId = 10000;
    private static final String FILE_PATH = "data/EnquiryList.csv";

    private EnquiryDatabase() {}

    public static EnquiryDatabase getInstance() {
        return instance;
    }

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
                        enquiry.setId(id); // 直接设置 ID
                        if (response != null) {
                            enquiry.reply(response);
                        }
                        enquiries.add(enquiry);
                        nextId = Math.max(nextId, id + 1);
                    }
                }
            }
        }
    }

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

    public void addEnquiry(Enquiry enquiry) {
        enquiry.setId(nextId++); // 设置 ID 并递增
        enquiries.add(enquiry);
    }
    public void removeEnquiry(Enquiry enquiry) {
        enquiries.remove(enquiry);
    }
    public List<Enquiry> findAll() {
        return enquiries;
    }

    public Enquiry findById(int id) {
        return enquiries.stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Enquiry> findByName(String name) {
        return enquiries.stream()
                .filter(e -> e.getUser().getName().equals(name))
                .collect(Collectors.toList());
    }

    public List<Enquiry> findByProject(Project project) {
        return enquiries.stream()
                .filter(e -> e.getProject().equals(project))
                .collect(Collectors.toList());
    }
}