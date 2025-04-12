package Service;

import Entity.Project;

import java.util.List;

public interface IProjectService {
    Project findProjectByName(String name);
    List<Project> findAllProjects();
    void addProject(Project project);
    void deleteProject(Project project);
    List<Project> findByManager(String managerName);

}
