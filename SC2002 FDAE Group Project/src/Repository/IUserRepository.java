package Repository;

import Entity.Applicant;
import Entity.HDBManager;
import Entity.HDBOfficer;
import Entity.Project;

import java.io.IOException;
import java.util.List;

public interface IUserRepository {
    void loadData() throws IOException;
    List<Applicant> findApplicants();
    List<HDBOfficer> findOfficers();
    List<HDBManager> findManagers();
}
