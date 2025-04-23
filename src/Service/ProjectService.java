package Service;

import Database.ProjectDatabase;
import Entity.FilterSettings;
import Entity.Project;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<Project> applyFilters(List<Project> projects, FilterSettings filterSettings) {
        List<Project> filteredProjects = new ArrayList<>(projects);

        // apply neighbourhood filter
        if (filterSettings.getNeighbourhood() != null) {
            filteredProjects = filteredProjects.stream()
                    .filter(p -> p.getNeighbourhood().equalsIgnoreCase(filterSettings.getNeighbourhood()))
                    .collect(Collectors.toList());
        }

        // apply flat type filter
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

        // apply price filter
        if (filterSettings.getMinPrice() != null) {
            final int minPrice = filterSettings.getMinPrice();
            filteredProjects = filteredProjects.stream()
                    .filter(p -> p.get2RoomPrice() >= minPrice || p.get3RoomPrice() >= minPrice)
                    .collect(Collectors.toList());
        }

        if (filterSettings.getMaxPrice() != null) {
            final int maxPrice = filterSettings.getMaxPrice();
            filteredProjects = filteredProjects.stream()
                    .filter(p -> p.get2RoomPrice() <= maxPrice || p.get3RoomPrice() <= maxPrice)
                    .collect(Collectors.toList());
        }

        // apply date filter
        if (filterSettings.getStartDate() != null) {
            final LocalDate startDate = filterSettings.getStartDate();
            filteredProjects = filteredProjects.stream()
                    .filter(p -> !p.getOpeningDate().isBefore(startDate))
                    .collect(Collectors.toList());
        }

        if (filterSettings.getEndDate() != null) {
            final LocalDate endDate = filterSettings.getEndDate();
            filteredProjects = filteredProjects.stream()
                    .filter(p -> !p.getClosingDate().isAfter(endDate))
                    .collect(Collectors.toList());
        }

        // apply available unit filter
        if (filterSettings.getMinAvailableUnits() != null) {
            final int minUnits = filterSettings.getMinAvailableUnits();
            filteredProjects = filteredProjects.stream()
                    .filter(p -> p.get2RoomUnits() >= minUnits || p.get3RoomUnits() >= minUnits)
                    .collect(Collectors.toList());
        }

        // apply officer slot filter
        if (filterSettings.getHasOfficerSlots() != null && filterSettings.getHasOfficerSlots()) {
            filteredProjects = filteredProjects.stream()
                    .filter(p -> p.getOfficerSlots() > 0)
                    .collect(Collectors.toList());
        }

        // apply sorting
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
