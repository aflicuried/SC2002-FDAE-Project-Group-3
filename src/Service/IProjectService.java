package Service;

import Entity.FilterSettings;
import Entity.Project;

import java.util.List;

public interface IProjectService {
    Project findProjectByName(String name);
    List<Project> findAllProjects();
    void addProject(Project project);
    void deleteProject(Project project);
    List<Project> findByManager(String managerName);
    List<Project> applyFilters(List<Project> projects, FilterSettings filterSettings);
}
