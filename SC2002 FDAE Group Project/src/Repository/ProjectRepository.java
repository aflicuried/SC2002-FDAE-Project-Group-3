package Repository;

import Entity.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ProjectRepository implements IProjectRepository {
    private List<Project> projects = new ArrayList<>();
    private final UserRepository userRepo; // dependency - UserRepository

    public ProjectRepository(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public void loadData() throws IOException {
        projects = readProjects("data/ApplicantList.csv");
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
            HDBManager manager = userRepo.findManagers().stream()
                    .filter(m -> m.getName().equals(managerName)).findFirst().orElse(null);

            int officeSlots = Integer.parseInt(data[11].trim());

            List<String> officerNames = Arrays.asList(data[12].replace("\"", "").split(","));
            List<HDBOfficer> officers = officerNames.stream()
                    .map(o -> userRepo.findOfficerByName(o.trim()))
                    .filter(Objects::nonNull) // filter Officer not found
                    .collect(Collectors.toList());

            Project project = new Project(name, neighbourhood, flatTypes, openingDate, closingDate, manager, officeSlots, officers);
            projects.add(project);
        }
        br.close();
        return projects;
    }

    public List<Project> findAll() {
        return new ArrayList<>(projects);//return copy to avoid edition or deletion!
    }
}
