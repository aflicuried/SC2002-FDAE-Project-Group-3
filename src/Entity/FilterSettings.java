package Entity;

import java.time.LocalDate;

/**
 * Represents a set of filter settings for filtering and sorting housing projects.
 */
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

    /**
     * Enum representing available sorting options.
     */
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

        /**
         * Returns the description of the sort type.
         *
         * @return a user-friendly description
         */
        public String getDescription() {
            return description;
        }
    }

    private SortType sortType;
    private boolean anyFilterApplied;

    /**
     * Constructs a FilterSettings object with default (unfiltered) values.
     */
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

    /**
     * Resets all filters to their default values (i.e., no filters applied).
     */
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

    /**
     * Checks whether any filter is currently applied.
     *
     * @return true if any filter is applied, false otherwise
     */
    public boolean isAnyFilterApplied() {
        return neighbourhood != null || flatType != null ||
                minPrice != null || maxPrice != null ||
                startDate != null || endDate != null ||
                minAvailableUnits != null || hasOfficerSlots != null;
    }

    /**
     * Returns a summary of the currently applied filters.
     *
     * @return a string describing the current filter settings
     */
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
            summary.append("Opening date=").append(startDate).append(" to ").append(endDate).append(", ");
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

    // Getters and Setters with Javadoc

    /** @return the selected neighbourhood filter */
    public String getNeighbourhood() {
        return neighbourhood;
    }

    /** @param neighbourhood sets the neighbourhood filter */
    public void setNeighbourhood(String neighbourhood) {
        this.neighbourhood = neighbourhood;
        this.anyFilterApplied = isAnyFilterApplied();
    }

    /** @return the selected flat type filter */
    public String getFlatType() {
        return flatType;
    }

    /** @param flatType sets the flat type filter */
    public void setFlatType(String flatType) {
        this.flatType = flatType;
        this.anyFilterApplied = isAnyFilterApplied();
    }

    /** @return the minimum price filter */
    public Integer getMinPrice() {
        return minPrice;
    }

    /** @param minPrice sets the minimum price filter */
    public void setMinPrice(Integer minPrice) {
        this.minPrice = minPrice;
        this.anyFilterApplied = isAnyFilterApplied();
    }

    /** @return the maximum price filter */
    public Integer getMaxPrice() {
        return maxPrice;
    }

    /** @param maxPrice sets the maximum price filter */
    public void setMaxPrice(Integer maxPrice) {
        this.maxPrice = maxPrice;
        this.anyFilterApplied = isAnyFilterApplied();
    }

    /** @return the starting date filter */
    public LocalDate getStartDate() {
        return startDate;
    }

    /** @param startDate sets the starting date filter */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
        this.anyFilterApplied = isAnyFilterApplied();
    }

    /** @return the ending date filter */
    public LocalDate getEndDate() {
        return endDate;
    }

    /** @param endDate sets the ending date filter */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
        this.anyFilterApplied = isAnyFilterApplied();
    }

    /** @return the minimum number of available units filter */
    public Integer getMinAvailableUnits() {
        return minAvailableUnits;
    }

    /** @param minAvailableUnits sets the minimum available units filter */
    public void setMinAvailableUnits(Integer minAvailableUnits) {
        this.minAvailableUnits = minAvailableUnits;
        this.anyFilterApplied = isAnyFilterApplied();
    }

    /** @return true if filtering by officer slot availability */
    public Boolean getHasOfficerSlots() {
        return hasOfficerSlots;
    }

    /** @param hasOfficerSlots sets the officer slots availability filter */
    public void setHasOfficerSlots(Boolean hasOfficerSlots) {
        this.hasOfficerSlots = hasOfficerSlots;
        this.anyFilterApplied = isAnyFilterApplied();
    }

    /** @return the selected sort type */
    public SortType getSortType() {
        return sortType;
    }

    /** @param sortType sets the desired sort type */
    public void setSortType(SortType sortType) {
        this.sortType = sortType;
    }
}
