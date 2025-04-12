package Database;

import Entity.FlatType;
import Entity.HDBManager;
import Entity.HDBOfficer;
import Entity.Project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectDatabase {
    private static final ProjectDatabase instance = new ProjectDatabase();
    private List<Project> projects = new ArrayList<>();
    private ProjectDatabase() {}    //private constructor; to avoid external instantiation
    private UserDatabase userDatabase = UserDatabase.getInstance();

    public static ProjectDatabase getInstance() {
        return instance;
    }

    public void loadData() throws IOException {
        projects = readProjects("data/ProjectList.csv");
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

            List<String> officerNames = Arrays.asList(data[12].replace("\"", "").split(","));

            List<HDBOfficer> officers = new ArrayList<>();
            for (String officerName : officerNames){
                HDBOfficer officer = userDatabase.findOfficers().stream()
                        .filter(m -> m.getName().equals(officerName)).findFirst().orElse(null);
                officers.add(officer);
            }

            Project project = new Project(name, neighbourhood, flatTypes, openingDate, closingDate, manager, officeSlots, officers);

            for (String officerName : officerNames){
                HDBOfficer officer = userDatabase.findOfficers().stream()
                        .filter(m -> m.getName().equals(officerName)).findFirst().orElse(null);
                if (officer != null) {
                    officer.setProjectHandling(project);
                }
            }
            projects.add(project);
        }
        br.close();
        return projects;
    }

    public List<Project> findProjects() {
        return new ArrayList<>(projects);   //return copy to avoid edition or deletion!
    }

    public Project findProjectByName(String name) {
        return findProjects().stream()
                .filter(p -> p.getName().equals(name)).findFirst().orElse(null);
    }

    public void addProject(Project project) {
        findProjects().add(project);
    }

    public void removeProject(Project project) {
        findProjects().remove(project);
    }

    public List<Project> findByManager(String name) {
        return findProjects().stream()
                .filter(p -> p.getManager().getName().equals(name)).collect(Collectors.toList());
    }
}