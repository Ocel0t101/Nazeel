package nazeel.data_types;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * This class models a full Guest Supplies Order used in the system.
 * It contains high-level metadata about the order (number, status, creator, date),
 * as well as the detailed list of items (supplies) attached to the order.
 */
public class Order {

    private String orderNo;
    private OrderStatus status;
    private String createdBy;
    private LocalDateTime createdDateTime;
    private List<OrderItem> items;
    private String url;

    // -------------------- Getters and Setters --------------------

    /**
     * @return the full URL for accessing this order in the system.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the URL for accessing the order.
     *
     * @param url full URL string
     */
    public void setUrl(String url) {
        this.url = url;
    }

    // -------------------- Enum Definition --------------------

    /**
     * Represents the current status of the order.
     */
    public enum OrderStatus {
        ACCOMPLISHED,
        PROPOSED,
        CANCELLED;

        /**
         * Converts a display string into an OrderStatus enum.
         *
         * @param status string label shown in UI (e.g. "Proposed")
         * @return OrderStatus enum or null if unrecognized
         */
        public static OrderStatus stringToStatus(String status) {
            return switch (status) {
                case "Accomplished" -> ACCOMPLISHED;
                case "Proposed" -> PROPOSED;
                case "Cancelled" -> CANCELLED;
                default -> null;
            };
        }
    }

    // -------------------- Constructors --------------------

    /**
     * Default constructor
     */
    public Order() {
    }

    /**
     * Full constructor for setting all order fields.
     */
    public Order(String orderNo, OrderStatus status, String createdBy, LocalDateTime createdDateTime, List<OrderItem> items, String url) {
        this.orderNo = orderNo;
        this.status = status;
        this.createdBy = createdBy;
        this.createdDateTime = createdDateTime;
        this.items = items;
        this.url = url;
    }

    // -------------------- Fluent Setters and Getters --------------------

    public String getOrderNo() {
        return orderNo;
    }

    public Order setOrderNo(String orderNo) {
        this.orderNo = orderNo;
        return this;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Order setStatus(OrderStatus status) {
        this.status = status;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Order setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    /**
     * Sets the creation datetime using a parsed LocalDateTime.
     */
    public Order setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    /**
     * Parses a string datetime and sets it (used from UI text values).
     *
     * @param dateTimeText UI date format string (e.g. "16/04/2025 02:27 PM")
     */
    public Order setCreatedDateTime(String dateTimeText) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a", Locale.ENGLISH);
        this.createdDateTime = LocalDateTime.parse(dateTimeText.trim(), formatter);
        return this;
    }

    /**
     * Formats the internal LocalDateTime back to a UI-friendly string.
     * Useful for verifying that parsed dates match the source.
     */
    public String getCreatedDateTimeAsString() {
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a", Locale.ENGLISH);
        return createdDateTime.format(outputFormatter);
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public Order setItems(List<OrderItem> items) {
        this.items = items;
        return this;
    }

    // -------------------- Debug Representation --------------------

    /**
     * @return A full string representation of this order, including all items
     */
    @Override
    public String toString() {
        return """
                Order {
                    orderNo= %s,
                    status= %s,
                    createdBy= %s,
                    dateTime= %s,
                    url = %s,
                    items= %s
                }
                """.formatted(orderNo, status, createdBy, getCreatedDateTimeAsString(), url, items);
    }

    // =========================================================================
    // Inner Class: OrderItem
    // =========================================================================

    /**
     * A nested item inside an Order representing one line of supply selection.
     */
    public static class OrderItem {

        private String category;
        private String supply;
        private int quantityPerUnit;
        private List<String> unitTypes;
        private List<String> unitsNumbers;
        private int total;

        /**
         * Default constructor.
         */
        public OrderItem() {
        }

        /**
         * Fully initializes an OrderItem and calculates total automatically.
         */
        public OrderItem(String category, String supply, int quantityPerUnit,
                         List<String> unitTypes, List<String> unitsNumbers) {
            this.category = category;
            this.supply = supply;
            this.quantityPerUnit = quantityPerUnit;
            this.unitTypes = unitTypes;
            this.unitsNumbers = unitsNumbers;
            this.total = unitsNumbers.size() * quantityPerUnit;
        }

        public String getCategory() {
            return category;
        }

        public OrderItem setCategory(String category) {
            this.category = category;
            return this;
        }

        public String getSupply() {
            return supply;
        }

        public OrderItem setSupply(String supply) {
            this.supply = supply;
            return this;
        }

        public int getQuantityPerUnit() {
            return quantityPerUnit;
        }

        public OrderItem setQuantityPerUnit(int quantityPerUnit) {
            this.quantityPerUnit = quantityPerUnit;
            return this;
        }

        public List<String> getUnitTypes() {
            return unitTypes;
        }

        public OrderItem setUnitTypes(List<String> unitTypes) {
            this.unitTypes = unitTypes;
            return this;
        }

        public List<String> getUnitsNumbers() {
            return unitsNumbers;
        }

        public OrderItem setUnitsNumbers(List<String> unitsNumber) {
            this.unitsNumbers = unitsNumber;
            return this;
        }

        public int getTotal() {
            return total;
        }

        public OrderItem setTotal(int total) {
            this.total = total;
            return this;
        }

        /**
         * @return A readable string showing this item's values
         */
        @Override
        public String toString() {
            return """
                    OrderItem {
                        category = %s,
                        supply = %s,
                        quantityPerUnit = %d,
                        unitTypes = %s,
                        units = %s,
                        total = %s
                    }
                    """.formatted(
                    category,
                    supply,
                    quantityPerUnit,
                    unitTypes,
                    unitsNumbers,
                    total
            );
        }
    }
}
