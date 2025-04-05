package Repository;

import Entity.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserRepository implements IUserRepository {
    public enum UserRole { APPLICANT, OFFICER, MANAGER };

    private List<Applicant> applicants = new ArrayList<>();
    private List<HDBOfficer> officers = new ArrayList<>();
    private List<HDBManager> managers = new ArrayList<>();

    public void loadData() throws IOException {
        applicants = read("data/ApplicantList.csv", UserRole.APPLICANT)
                .stream().map(u -> (Applicant)u).collect(Collectors.toList());
        officers = read("data/OfficerList.csv", UserRole.OFFICER)
                .stream().map(u -> (HDBOfficer)u).collect(Collectors.toList());
        managers = read("data/ManagerList.csv", UserRole.MANAGER)
                .stream().map(u -> (HDBManager)u).collect(Collectors.toList()); //downcasting
    }

    public List<User> read(String filePath, UserRole role) throws IOException {
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
                case APPLICANT:
                    user = new Applicant(name, nric, age, status, password);
                    break;
                case OFFICER:
                    user = new HDBOfficer(name, nric, age, status, password);
                    break;
                case MANAGER:
                    user = new HDBManager(name, nric, age, status, password);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid role");
            }
            users.add(user);
        }
        br.close();
        return users;
    }

    public List<Applicant> findApplicants() {
        return new ArrayList<>(applicants);//return copy to avoid edition or deletion!
    }

    public List<HDBOfficer> findOfficers() {
        return new ArrayList<>(officers);//return copy to avoid edition or deletion!
    }

    public List<HDBManager> findManagers() {
        return new ArrayList<>(managers);//return copy to avoid edition or deletion!
    }



    /*public User findByNric(String nric) {
        return Stream.concat(
                        applicants.stream(),
                        Stream.concat(officers.stream(), managers.stream()) //concat all the lists temporarily and search
                )
                .filter(user -> user.getNric().equals(nric)) //can also iterate or search separately
                .findFirst()
                .orElse(null);
    }*/

    public HDBOfficer findOfficerByName(String name) {
        return officers.stream()
                .filter(officer -> officer.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null); // 未找到返回 null
    }

    /*
    public void addApplicant(Applicant applicant) {
        this.applicants.add(applicant);
    }
    public void addOfficer(HDBOfficer officer) {
        this.officers.add(officer);
    }
    public void addManager(HDBManager manager) {
        this.managers.add(manager);
    }
    public void removeApplicant(Applicant applicant) {
        this.applicants.remove(applicant);
    }*/
}