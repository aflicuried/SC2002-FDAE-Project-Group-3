package Entity;

import java.time.LocalDate;

public class FilterSettings {
    // Basic filtering condition
    private String neighbourhood;
    private String flatType;

    // Filter by price
    private Integer minPrice;
    private Integer maxPrice;

    // Filter by date
    private LocalDate startDate;
    private LocalDate endDate;

    // Filter by available units
    private Integer minAvailableUnits;

    // Filter by officer slots
    private Boolean hasOfficerSlots;

    // Sort methods
    public enum SortType {
        NAME_ASC("Sort in ascending order by name"),
        NAME_DESC("Sort in descending order by name"),
        PRICE_ASC("Sort in ascending order by price"),
        PRICE_DESC("Sort in descending order by price"),
        DATE_ASC("Sort in ascending order by opening date"),
        DATE_DESC("Sort in descending order by opening date");

        private final String description;

        SortType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    private SortType sortType;

    // trace if user has used some filters
    private boolean anyFilterApplied;

    // default: ascending order
    public FilterSettings() {
        this.neighbourhood = null;
        this.flatType = null;
        this.minPrice = null;
        this.maxPrice = null;
        this.startDate = null;
        this.endDate = null;
        this.minAvailableUnits = null;
        this.hasOfficerSlots = null;
        this.sortType = SortType.NAME_ASC;
        this.anyFilterApplied = false;
    }

    // reset all filters
    public void resetAllFilters() {
        this.neighbourhood = null;
        this.flatType = null;
        this.minPrice = null;
        this.maxPrice = null;
        this.startDate = null;
        this.endDate = null;
        this.minAvailableUnits = null;
        this.hasOfficerSlots = null;
        this.anyFilterApplied = false;
    }

    // check if any filter is applied
    public boolean isAnyFilterApplied() {
        return neighbourhood != null || flatType != null ||
                minPrice != null || maxPrice != null ||
                startDate != null || endDate != null ||
                minAvailableUnits != null || hasOfficerSlots != null;
    }

    // return description of current filters
    public String getFilterSummary() {
        StringBuilder summary = new StringBuilder("Current Filter Condition: ");

        if (!isAnyFilterApplied()) {
            summary.append("None");
            return summary.toString();
        }

        if (neighbourhood != null) {
            summary.append("Location=").append(neighbourhood).append(", ");
        }

        if (flatType != null) {
            summary.append("Flat Type=").append(flatType).append(", ");
        }

        if (minPrice != null && maxPrice != null) {
            summary.append("Price range=").append(minPrice).append("-").append(maxPrice).append(", ");
        } else if (minPrice != null) {
            summary.append("Minimum Price=").append(minPrice).append(", ");
        } else if (maxPrice != null) {
            summary.append("Maximum Price=").append(maxPrice).append(", ");
        }

        if (startDate != null && endDate != null) {
            summary.append("Opening date=").append(startDate).append("to").append(endDate).append(", ");
        } else if (startDate != null) {
            summary.append("Opening date not earlier than=").append(startDate).append(", ");
        } else if (endDate != null) {
            summary.append("Opening date not later than=").append(endDate).append(", ");
        }

        if (minAvailableUnits != null) {
            summary.append("Minimum available units=").append(minAvailableUnits).append(", ");
        }

        if (hasOfficerSlots != null) {
            summary.append("Has officer slots=").append(hasOfficerSlots ? "Yes" : "No").append(", ");
        }

        if (summary.toString().endsWith(", ")) {
            summary.setLength(summary.length() - 2);
        }

        summary.append(" | Sorting order: ").append(sortType.getDescription());

        return summary.toString();
    }

    // Getters and setters
    public String getNeighbourhood() {
        return neighbourhood;
    }

    public void setNeighbourhood(String neighbourhood) {
        this.neighbourhood = neighbourhood;
        this.anyFilterApplied = isAnyFilterApplied();
    }

    public String getFlatType() {
        return flatType;
    }

    public void setFlatType(String flatType) {
        this.flatType = flatType;
        this.anyFilterApplied = isAnyFilterApplied();
    }

    public Integer getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Integer minPrice) {
        this.minPrice = minPrice;
        this.anyFilterApplied = isAnyFilterApplied();
    }

    public Integer getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Integer maxPrice) {
        this.maxPrice = maxPrice;
        this.anyFilterApplied = isAnyFilterApplied();
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
        this.anyFilterApplied = isAnyFilterApplied();
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
        this.anyFilterApplied = isAnyFilterApplied();
    }

    public Integer getMinAvailableUnits() {
        return minAvailableUnits;
    }

    public void setMinAvailableUnits(Integer minAvailableUnits) {
        this.minAvailableUnits = minAvailableUnits;
        this.anyFilterApplied = isAnyFilterApplied();
    }

    public Boolean getHasOfficerSlots() {
        return hasOfficerSlots;
    }

    public void setHasOfficerSlots(Boolean hasOfficerSlots) {
        this.hasOfficerSlots = hasOfficerSlots;
        this.anyFilterApplied = isAnyFilterApplied();
    }

    public SortType getSortType() {
        return sortType;
    }

    public void setSortType(SortType sortType) {
        this.sortType = sortType;
    }
}