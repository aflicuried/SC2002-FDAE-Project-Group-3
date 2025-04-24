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
 * Service class that provides functionalities to manage and filter HDB projects.
 */
public class ProjectService implements IProjectService {
    // Database instance
    private ProjectDatabase projectDatabase = ProjectDatabase.getInstance();

    /**
     * Finds a project by its name.
     *
     * @param name the name of the project
     * @return the matching Project, or null if not found
     */
    public Project findProjectByName(String name) {
        return projectDatabase.findProjectByName(name);
    }

    /**
     * Retrieves all projects from the database.
     *
     * @return list of all projects
     */
    public List<Project> findAllProjects() {
        return projectDatabase.findProjects();
    }

    /**
     * Adds a new project to the database.
     *
     * @param project the project to be added
     */
    public void addProject(Project project) {
        projectDatabase.addProject(project);
    }

    /**
     * Deletes a project from the database.
     *
     * @param project the project to be removed
     */
    public void deleteProject(Project project) {
        projectDatabase.removeProject(project);
    }

    /**
     * Finds projects that are managed by a specific manager.
     *
     * @param managerName the name of the manager
     * @return list of projects managed by the specified manager
     */
    public List<Project> findByManager(String managerName) {
        return projectDatabase.findByManager(managerName);
    }

    /**
     * Applies a set of filters and sorting options to a list of projects.
     *
     * @param projects        the list of projects to filter
     * @param filterSettings  the settings used to filter and sort projects
     * @return list of filtered and sorted projects
     */
    public List<Project> applyFilters(List<Project> projects, FilterSettings filterSettings) {
        List<Project> filteredProjects = new ArrayList<>(projects);

        // Filter by neighbourhood
        if (filterSettings.getNeighbourhood() != null) {
            filteredProjects = filteredProjects.stream()
                    .filter(p -> p.getNeighbourhood().equalsIgnoreCase(filterSettings.getNeighbourhood()))
                    .collect(Collectors.toList());
        }

        // Filter by flat type availability
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

        // Filter by minimum price
        if (filterSettings.getMinPrice() != null) {
            final int minPrice = filterSettings.getMinPrice();
            filteredProjects = filteredProjects.stream()
                    .filter(p -> p.get2RoomPrice() >= minPrice || p.get3RoomPrice() >= minPrice)
                    .collect(Collectors.toList());
        }

        // Filter by maximum price
        if (filterSettings.getMaxPrice() != null) {
            final int maxPrice = filterSettings.getMaxPrice();
            filteredProjects = filteredProjects.stream()
                    .filter(p -> p.get2RoomPrice() <= maxPrice || p.get3RoomPrice() <= maxPrice)
                    .collect(Collectors.toList());
        }

        // Filter by project opening start date
        if (filterSettings.getStartDate() != null) {
            final LocalDate startDate = filterSettings.getStartDate();
            filteredProjects = filteredProjects.stream()
                    .filter(p -> !p.getOpeningDate().isBefore(startDate))
                    .collect(Collectors.toList());
        }

        // Filter by project closing end date
        if (filterSettings.getEndDate() != null) {
            final LocalDate endDate = filterSettings.getEndDate();
            filteredProjects = filteredProjects.stream()
                    .filter(p -> !p.getClosingDate().isAfter(endDate))
                    .collect(Collectors.toList());
        }

        // Filter by minimum available units
        if (filterSettings.getMinAvailableUnits() != null) {
            final int minUnits = filterSettings.getMinAvailableUnits();
            filteredProjects = filteredProjects.stream()
                    .filter(p -> p.get2RoomUnits() >= minUnits || p.get3RoomUnits() >= minUnits)
                    .collect(Collectors.toList());
        }

        // Filter projects that have available officer slots
        if (filterSettings.getHasOfficerSlots() != null && filterSettings.getHasOfficerSlots()) {
            filteredProjects = filteredProjects.stream()
                    .filter(p -> p.getOfficerSlots() > 0)
                    .collect(Collectors.toList());
        }

        if (filterSettings.getHasOfficerSlots() != null && !filterSettings.getHasOfficerSlots()) {
            filteredProjects = filteredProjects.stream()
                    .filter(p -> p.getOfficerSlots() == 0)
                    .collect(Collectors.toList());
        }

        // Apply sorting based on user preference
        switch (filterSettings.getSortType()) {
            case NAME_ASC:
                filteredProjects.sort(Comparator.comparing(Project::getName));
                break;
            case NAME_DESC:
                filteredProjects.sort(Comparator.comparing(Project::getName).reversed());
                break;
            case PRICE_ASC:
                filteredProjects.sort(Comparator.comparingInt((Project p) -> Math.min(p.get2RoomPrice(), p.get3RoomPrice())));
                break;
            case PRICE_DESC:
                filteredProjects.sort(Comparator.comparingInt((Project p) -> Math.min(p.get2RoomPrice(), p.get3RoomPrice())).reversed());
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
