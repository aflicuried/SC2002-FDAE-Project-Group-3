package Service;

import Entity.FilterSettings;
import Entity.Project;

import java.util.List;


/**
 * Interface for services related to managing and retrieving project data.
 * Provides methods for CRUD operations on projects and applying filters to project lists.
 */
public interface IProjectService {


    /**
     * Finds a project by its name.
     *
     * @param name the name of the project
     * @return the Project with the specified name, or null if not found
     */
    Project findProjectByName(String name);


    /**
     * Retrieves all projects in the system.
     *
     * @return a list of all Project objects
     */
    List<Project> findAllProjects();


    /**
     * Adds a new project to the system.
     *
     * @param project the Project object to be added
     */
    void addProject(Project project);


    /**
     * Deletes a project from the system.
     *
     * @param project the Project object to be deleted
     */
    void deleteProject(Project project);


    /**
     * Finds all projects managed by a specific manager.
     *
     * @param managerName the name of the manager
     * @return a list of Project objects managed by the specified manager
     */
    List<Project> findByManager(String managerName);


    /**
     * Applies filter settings to a list of projects.
     *
     * @param projects the list of projects to filter
     * @param filterSettings the filtering criteria
     * @return a list of projects that match the filter criteria
     */
    List<Project> applyFilters(List<Project> projects, FilterSettings filterSettings);
}
