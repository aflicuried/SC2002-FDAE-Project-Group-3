package Service;

import Database.ProjectDatabase;
import Entity.Project;

import java.util.List;

public class ProjectService implements IProjectService {
    //database
    private ProjectDatabase projectDatabase = ProjectDatabase.getInstance();

    //methods
    public Project findProjectByName(String name) {
        return projectDatabase.findProjectByName(name);
    }

    public List<Project> findAllProjects() {
        return projectDatabase.findProjects();
    }

    public void addProject(Project project) {
        projectDatabase.addProject(project);
    }

    public void deleteProject(Project project) {
        projectDatabase.removeProject(project);
    }

    public List<Project> findByManager(String managerName) {
        return projectDatabase.findByManager(managerName);
    }
}
