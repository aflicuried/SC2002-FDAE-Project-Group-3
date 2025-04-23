package Database;

import Entity.FlatType;
import Entity.HDBManager;
import Entity.HDBOfficer;
import Entity.Project;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectDatabase {
    private static final ProjectDatabase instance = new ProjectDatabase();
    private List<Project> projects = new ArrayList<>();
    private ProjectDatabase() {}    //private constructor; to avoid external instantiation
    private UserDatabase userDatabase = UserDatabase.getInstance();
    private static final String FILE_PATH = "data/ProjectList.csv";

    public static ProjectDatabase getInstance() {
        return instance;
    }

    public void loadData() throws IOException {
        projects = readProjects(FILE_PATH);
    }

    public List<Project> readProjects(String filePath) throws IOException {
        List<Project> projects = new ArrayList<>();
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
            String neighbourhood = data[1].trim();

            List<FlatType> flatTypes = new ArrayList<>();
            flatTypes.add(new FlatType(data[2].trim(), Integer.parseInt(data[3].trim()), Integer.parseInt(data[4].trim())));
            flatTypes.add(new FlatType(data[5].trim(), Integer.parseInt(data[6].trim()), Integer.parseInt(data[7].trim())));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/M/d");
            LocalDate openingDate = LocalDate.parse(data[8].trim(), formatter);
            LocalDate closingDate = LocalDate.parse(data[9].trim(), formatter);

            String managerName = data[10].trim();

            HDBManager manager = userDatabase.findManagers().stream()
                    .filter(m -> m.getName().equals(managerName)).findFirst().orElse(null);

            int officeSlots = Integer.parseInt(data[11].trim());

            String[] officerNames = new String[officeSlots-1];
            for (int i = 12; i < data.length; i++) {
                officerNames[i-12] = data[i].replace("\"", "");
            }

            List<HDBOfficer> officers = new ArrayList<>();
            for (String officerName : officerNames){
                HDBOfficer officer = userDatabase.findOfficers().stream()
                        .filter(m -> m.getName().equals(officerName)).findFirst().orElse(null);
                officers.add(officer);
            }

            Project project = new Project(name, neighbourhood, flatTypes, openingDate, closingDate, manager, officeSlots, officers);
            project.setVisibility(true);
            projects.add(project);

            for (String officerName : officerNames){
                HDBOfficer officer = userDatabase.findOfficers().stream()
                        .filter(m -> m.getName().equals(officerName))
                        .findFirst().orElse(null);
                if (officer != null) {
                    officer.setProjectHandling(project);
                }
            }

        }
        br.close();
        return projects;
    }

    public void saveData() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            // Write header line
            writer.write("Project Name,Neighbourhood,Type 1,Number of units for Type 1,Selling price for Type 1," +
                    "Type 2,Number of units for Type 2,Selling price for Type 2," +
                    "Application opening date,Application closing date,Manager,Officer Slots,Officer");
            writer.newLine();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/M/d");

            for (Project project : projects) {
                // 处理 officers 列表，所有 officer 名字放在一个带引号的字符串中，用逗号分隔
                StringBuilder officersStr = new StringBuilder();
                List<HDBOfficer> officerList = project.getOfficers();
                if (officerList != null && !officerList.isEmpty()) {
                    officersStr.append("\"");
                    boolean firstOfficer = true;
                    for (HDBOfficer officer : officerList) {
                        if (officer != null) {
                            if (!firstOfficer) {
                                officersStr.append(",");
                            }
                            officersStr.append(officer.getName());
                            firstOfficer = false;
                        }
                    }
                    officersStr.append("\"");
                }

                // Format the project data according to the CSV structure
                String line = String.format("%s,%s,%s,%d,%d,%s,%d,%d,%s,%s,%s,%d,%s",
                        project.getName(),
                        project.getNeighbourhood(),
                        "2-Room",  // Type 1 is always 2-Room
                        project.get2RoomUnits(),
                        project.get2RoomPrice(),
                        "3-Room",  // Type 2 is always 3-Room
                        project.get3RoomUnits(),
                        project.get3RoomPrice(),
                        project.getOpeningDate().format(formatter),
                        project.getClosingDate().format(formatter),
                        project.getManager().getName(),
                        project.getOfficerSlots(),
                        officersStr.toString());

                writer.write(line);
                writer.newLine();
            }
        }
    }

    public List<Project> findProjects() {
        return projects;
    }

    public Project findProjectByName(String name) {
        return findProjects().stream()
                .filter(p -> p.getName().equals(name)).findFirst().orElse(null);
    }

    public void addProject(Project project) {
        findProjects().add(project);
        project.setVisibility(true);
    }

    public void removeProject(Project project) {
        findProjects().remove(project);
    }

    public List<Project> findByManager(String name) {
        return findProjects().stream()
                .filter(p -> p.getManager().getName().equals(name)).collect(Collectors.toList());
    }
}