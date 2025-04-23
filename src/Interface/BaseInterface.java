package Interface;

import Entity.FilterSettings;
import Entity.Project;
import Entity.User;
import Service.AuthService;
import Service.IProjectService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Abstract base class for all user interface types (e.g., ApplicantInterface, OfficerInterface).
 * Provides shared functionality such as filter management, input reading,
 * password changing, and project filtering.
 */
public abstract class BaseInterface {
    Scanner sc = new Scanner(System.in);
    final User currentUser;
    int choice;
    protected FilterSettings filterSettings;
    protected IProjectService projectService;

    /**
     * Constructs a BaseInterface with the given user.
     * @param currentUser the currently logged-in user
     */
    public BaseInterface(User currentUser) {
        this.currentUser = currentUser;
        this.filterSettings = new FilterSettings();
    }

    /**
     * Reads an integer input from the user with validation.
     * @param prompt the message to prompt the user
     * @return the valid integer input
     */
    protected int readIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int input = sc.nextInt();
                sc.nextLine();
                return input;
            } catch (InputMismatchException e) {
                sc.nextLine();
                System.out.println("Please enter a valid number.");
            }
        }
    }

    
    /**
     * Prompts the user for a date and parses it into a LocalDate.
     * Expected format: yyyy/M/d
     * @param prompt the prompt message
     * @return the parsed LocalDate
     */
    protected LocalDate parseDate(String prompt) {
        while (true) {
            System.out.println(prompt);
            try {
                String date = sc.nextLine();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/M/d");
                return LocalDate.parse(date, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format, try again.");
            }
        }
    }


    /**
     * Starts the user interface. Each subclass must implement its own interface logic.
     */
    public abstract void start();


    /**
     * Sets the project service used for filtering and project operations.
     * @param projectService the project service to set
     */
    protected void setProjectService(IProjectService projectService) {
        this.projectService = projectService;
    }


    /**
     * Allows the user to change their password if the old password is verified.
     * @return true if password change was successful, false otherwise
     */
    protected boolean changePassword() {
        AuthService authService = new AuthService();
        System.out.println("Enter your old password: ");
        String oldPassword = sc.next();
        sc.nextLine();
        if (authService.checkUser(currentUser.getNric(), oldPassword)){
            System.out.println("Enter your new password: ");
            String newPassword = sc.next();
            sc.nextLine();
            if (newPassword != null && !newPassword.trim().isEmpty()) {
                authService.changePassword(currentUser, newPassword);
                System.out.println("Password changed successfully.");
                return true;
            }
            else
                System.out.println("Invalid password.");
        }
        else {
            System.out.println("Incorrect old password.");
        }
        return false;
    }


    /**
     * Provides a console menu to manage filters applied to project listings.
     */
    protected void manageFilters() {
        while (true) {
            System.out.println("\nProject Filter Settings:");
            System.out.println(filterSettings.getFilterSummary());
            System.out.println("\n1 - Set Neighbourhood Filter");
            System.out.println("2 - Set Flat Type Filter");
            System.out.println("3 - Set Price Range");
            System.out.println("4 - Set Application Date Range");
            System.out.println("5 - Set Minimum Available Units");
            System.out.println("6 - Filter Projects with Officer Slots");
            System.out.println("7 - Change Sort Order");
            System.out.println("8 - Reset All Filters");
            System.out.println("9 - Return to Previous Menu");
            choice = readIntInput("Enter your choice: ");

            switch (choice) {
                case 1 -> {
                    System.out.println("Enter neighbourhood name (or leave empty to clear this filter): ");
                    String neighbourhood = sc.nextLine();
                    filterSettings.setNeighbourhood(neighbourhood.isEmpty() ? null : neighbourhood);
                }
                case 2 -> {
                    System.out.println("Select flat type filter:");
                    System.out.println("1 - 2-Room");
                    System.out.println("2 - 3-Room");
                    System.out.println("3 - Clear this filter");
                    int flatChoice = readIntInput("Enter your choice: ");

                    if (flatChoice == 1) {
                        filterSettings.setFlatType("2-Room");
                    } else if (flatChoice == 2) {
                        filterSettings.setFlatType("3-Room");
                    } else if (flatChoice == 3) {
                        filterSettings.setFlatType(null);
                    } else {
                        System.out.println("Invalid choice. Filter not changed.");
                    }
                }
                case 3 -> {
                    int minPrice = readIntInput("Enter minimum price (or -1 to clear this filter): ");
                    filterSettings.setMinPrice(minPrice < 0 ? null : minPrice);
                    int maxPrice = readIntInput("Enter maximum price (or -1 to clear this filter): ");
                    filterSettings.setMaxPrice(maxPrice < 0 ? null : maxPrice);
                }
                case 4 -> {
                    try {
                        System.out.println("Enter earliest application opening date (format: yyyy/M/d, or leave empty to clear): ");
                        String startDateStr = sc.nextLine();
                        if (!startDateStr.isEmpty()) {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/M/d");
                            LocalDate startDate = LocalDate.parse(startDateStr, formatter);
                            filterSettings.setStartDate(startDate);
                        } else {
                            filterSettings.setStartDate(null);
                        }

                        System.out.println("Enter latest application closing date (format: yyyy/M/d, or leave empty to clear): ");
                        String endDateStr = sc.nextLine();
                        if (!endDateStr.isEmpty()) {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/M/d");
                            LocalDate endDate = LocalDate.parse(endDateStr, formatter);
                            filterSettings.setEndDate(endDate);
                        } else {
                            filterSettings.setEndDate(null);
                        }
                    } catch (Exception e) {
                        System.out.println("Error parsing date. Please use format yyyy/M/d.");
                    }
                }
                case 5 -> {
                    int minUnits = readIntInput("Enter minimum number of available units (or -1 to clear this filter): ");
                    filterSettings.setMinAvailableUnits(minUnits < 0 ? null : minUnits);
                }
                case 6 -> {
                    System.out.println("Filter projects with available officer slots?");
                    System.out.println("1 - Yes (show only projects with available slots)");
                    System.out.println("2 - No (show only projects without available slots)");
                    System.out.println("3 - Clear this filter (show all projects)");
                    int slotChoice = readIntInput("Enter your choice: ");

                    if (slotChoice == 1) {
                        filterSettings.setHasOfficerSlots(true);
                    } else if (slotChoice == 2) {
                        filterSettings.setHasOfficerSlots(false);
                    } else if (slotChoice == 3) {
                        filterSettings.setHasOfficerSlots(null);
                    } else {
                        System.out.println("Invalid choice. Filter not changed.");
                    }
                }
                case 7 -> {
                    System.out.println("Select sort order:");
                    FilterSettings.SortType[] sortTypes = FilterSettings.SortType.values();
                    for (int i = 0; i < sortTypes.length; i++) {
                        System.out.println((i + 1) + " - " + sortTypes[i].getDescription());
                    }

                    int sortChoice = sc.nextInt();
                    sc.nextLine();

                    if (sortChoice >= 1 && sortChoice <= sortTypes.length) {
                        filterSettings.setSortType(sortTypes[sortChoice - 1]);
                    } else {
                        System.out.println("Invalid choice. Sort order not changed.");
                    }
                }
                case 8 -> {
                    filterSettings.resetAllFilters();
                    System.out.println("All filters have been reset.");
                }
                case 9 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }


    /**
     * Applies the current set of filters to a list of projects using the project service.
     * @param projects the original list of projects
     * @return a filtered list of projects
     */
    protected List<Project> applyProjectFilters(List<Project> projects) {
        if (projectService == null) {
            System.out.println("Warning: Project service not set. Filters will not be applied.");
            return projects;
        }

        // apply filters
        List<Project> filteredProjects = projectService.applyFilters(projects, filterSettings);

        // show the filter
        if (filterSettings.isAnyFilterApplied()) {
            System.out.println("Applied filters: " + projects.size() + " projects filtered to " +
                    filteredProjects.size() + " projects.");
        }

        return filteredProjects;
    }
}
