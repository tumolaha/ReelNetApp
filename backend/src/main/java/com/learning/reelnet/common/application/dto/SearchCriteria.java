package com.learning.reelnet.common.application.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a search criterion with field, operator and value.
 * Used for building dynamic queries.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchCriteria {

    /**
     * The field to search on.
     */
    private String field;

    /**
     * The operator to use (e.g., ":", "=", ">", "<", "!", "~").
     */
    private SearchOperator operator;

    /**
     * The value to search for.
     */
    private Object value;

    /**
     * Enum representing supported search operators.
     */
    public enum SearchOperator {
        EQUALS("="),           // Equals
        NOT_EQUALS("!"),       // Not equals
        GREATER_THAN(">"),     // Greater than
        LESS_THAN("<"),        // Less than
        GREATER_EQUAL(">="),   // Greater than or equal
        LESS_EQUAL("<="),      // Less than or equal
        LIKE(":"),             // Contains (like %value%)
        STARTS_WITH("^"),      // Starts with (like value%)
        ENDS_WITH("$"),        // Ends with (like %value)
        IN("@"),               // In a collection
        BETWEEN("~");          // Between two values

        private final String symbol;

        SearchOperator(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() {
            return symbol;
        }

        /**
         * Get operator from symbol.
         *
         * @param symbol the operator symbol
         * @return the matching operator
         */
        public static SearchOperator fromSymbol(String symbol) {
            for (SearchOperator operator : values()) {
                if (operator.getSymbol().equals(symbol)) {
                    return operator;
                }
            }
            return EQUALS; // Default
        }
    }

    /**
     * Parse a string representation of a search criterion.
     * Format: "field:value" or "field=value" or "field>value", etc.
     *
     * @param searchString the string to parse
     * @return the parsed search criterion
     */
    public static SearchCriteria parse(String searchString) {
        // Find the operator symbol
        int operatorIndex = -1;
        SearchOperator operator = SearchOperator.EQUALS;

        for (SearchOperator op : SearchOperator.values()) {
            int index = searchString.indexOf(op.getSymbol());
            if (index > 0 && (operatorIndex == -1 || index < operatorIndex)) {
                operatorIndex = index;
                operator = op;
            }
        }

        if (operatorIndex == -1) {
            // Default: treat the whole string as a search term for all searchable fields
            return new SearchCriteria("_all", SearchOperator.LIKE, searchString);
        }

        String field = searchString.substring(0, operatorIndex).trim();
        String value = searchString.substring(operatorIndex + operator.getSymbol().length()).trim();

        return new SearchCriteria(field, operator, parseValue(value, operator));
    }

    /**
     * Parse the value based on operator.
     *
     * @param value    the string value
     * @param operator the operator
     * @return the parsed value
     */
    private static Object parseValue(String value, SearchOperator operator) {
        if (operator == SearchOperator.IN) {
            // Parse comma-separated values for IN operator
            return List.of(value.split(","));
        } else if (operator == SearchOperator.BETWEEN) {
            // Parse range for BETWEEN operator
            String[] parts = value.split(",");
            if (parts.length == 2) {
                return parts;
            }
        }
        return value;
    }
}