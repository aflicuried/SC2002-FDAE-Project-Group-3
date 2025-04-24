package Service;

import Database.ProjectDatabase;
import Entity.FilterSettings;
import Entity.Project;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides services for managing {@link Project} entities, including adding,
 * removing, finding, and filtering projects. This service interacts with the
 * {@link ProjectDatabase} as its data source.
 * 
 * <p>
 * Implements the {@link IProjectService} interface.
 *
 */
public class ProjectService implements IProjectService {

    private ProjectDatabase projectDatabase = ProjectDatabase.getInstance();

    /**
     * Finds and returns a project by its name.
     *
     * @param name the name of the project to find
     * @return the {@link Project} with the given name, or null if not found
     */
    public Project findProjectByName(String name) {
        return projectDatabase.findProjectByName(name);
    }

    /**
     * Retrieves all available projects from the database.
     *
     * @return a list of all {@link Project} instances
     */
    public List<Project> findAllProjects() {
        return projectDatabase.findProjects();
    }

    /**
     * Adds a new project to the database.
     *
     * @param project the {@link Project} to be added
     */
    public void addProject(Project project) {
        projectDatabase.addProject(project);
    }

    /**
     * Removes a project from the database.
     *
     * @param project the {@link Project} to be removed
     */
    public void deleteProject(Project project) {
        projectDatabase.removeProject(project);
    }

    /**
     * Retrieves all projects managed by a specific manager.
     *
     * @param managerName the name of the manager
     * @return a list of projects managed by the specified manager
     */
    public List<Project> findByManager(String managerName) {
        return projectDatabase.findByManager(managerName);
    }

    /**
     * Filters and sorts a list of projects based on the specified {@link FilterSettings}.
     * Supports filtering by neighbourhood, flat type, price range, date range, availability, and officer slots.
     * Also supports sorting by name, price, or opening date in ascending or descending order.
     *
     * @param projects the list of projects to filter
     * @param filterSettings the settings to apply during filtering and sorting
     * @return a list of projects that meet the filter criteria
     */
    public List<Project> applyFilters(List<Project> projects, FilterSettings filterSettings) {
        List<Project> filteredProjects = new ArrayList<>(projects);

        // Filtering logic
        if (filterSettings.getNeighbourhood() != null) {
            filteredProjects = filteredProjects.stream()
                    .filter(p -> p.getNeighbourhood().equalsIgnoreCase(filterSettings.getNeighbourhood()))
                    .collect(Collectors.toList());
        }

        if (filterSettings.getFlatType() != null) {
            filteredProjects = filteredProjects.stream()
                    .filter(p -> {
                        if (filterSettings.getFlatType().equals("2-Room")) {
                            return p.get2RoomUnits() > 0;
                        } else if (filterSettings.getFlatType().equals("3-Room")) {
                            return p.get3RoomUnits() > 0;
                        }
                        return true;
                    })
                    .collect(Collectors.toList());
        }

        if (filterSettings.getMinPrice() != null) {
            int minPrice = filterSettings.getMinPrice();
            filteredProjects = filteredProjects.stream()
                    .filter(p -> p.get2RoomPrice() >= minPrice || p.get3RoomPrice() >= minPrice)
                    .collect(Collectors.toList());
        }

        if (filterSettings.getMaxPrice() != null) {
            int maxPrice = filterSettings.getMaxPrice();
            filteredProjects = filteredProjects.stream()
                    .filter(p -> p.get2RoomPrice() <= maxPrice || p.get3RoomPrice() <= maxPrice)
                    .collect(Collectors.toList());
        }

        if (filterSettings.getStartDate() != null) {
            LocalDate startDate = filterSettings.getStartDate();
            filteredProjects = filteredProjects.stream()
                    .filter(p -> !p.getOpeningDate().isBefore(startDate))
                    .collect(Collectors.toList());
        }

        if (filterSettings.getEndDate() != null) {
            LocalDate endDate = filterSettings.getEndDate();
            filteredProjects = filteredProjects.stream()
                    .filter(p -> !p.getClosingDate().isAfter(endDate))
                    .collect(Collectors.toList());
        }

        if (filterSettings.getMinAvailableUnits() != null) {
            int minUnits = filterSettings.getMinAvailableUnits();
            filteredProjects = filteredProjects.stream()
                    .filter(p -> p.get2RoomUnits() >= minUnits || p.get3RoomUnits() >= minUnits)
                    .collect(Collectors.toList());
        }

        if (filterSettings.getHasOfficerSlots() != null) {
            if (filterSettings.getHasOfficerSlots()) {
                filteredProjects = filteredProjects.stream()
                        .filter(p -> p.getOfficerSlots() > 0)
                        .collect(Collectors.toList());
            } else {
                filteredProjects = filteredProjects.stream()
                        .filter(p -> p.getOfficerSlots() == 0)
                        .collect(Collectors.toList());
            }
        }

        // Sorting logic
        switch (filterSettings.getSortType()) {
            case NAME_ASC:
                filteredProjects.sort(Comparator.comparing(Project::getName));
                break;
            case NAME_DESC:
                filteredProjects.sort(Comparator.comparing(Project::getName).reversed());
                break;
            case PRICE_ASC:
                filteredProjects.sort(Comparator.comparingInt(
                        p -> Math.min(p.get2RoomPrice(), p.get3RoomPrice())));
                break;
            case PRICE_DESC:
                filteredProjects.sort(Comparator.comparingInt(
                        p -> Math.min(p.get2RoomPrice(), p.get3RoomPrice())).reversed());
                break;
            case DATE_ASC:
                filteredProjects.sort(Comparator.comparing(Project::getOpeningDate));
                break;
            case DATE_DESC:
                filteredProjects.sort(Comparator.comparing(Project::getOpeningDate).reversed());
                break;
        }

        return filteredProjects;
    }
}
