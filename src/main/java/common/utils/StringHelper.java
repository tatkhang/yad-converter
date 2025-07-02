package common.utils;

import common.exceptions.YadConverterException;

public class StringHelper {
    /**
     * Checks if a string is {@code null} or consists only of whitespace characters.
     *
     * @param value the string to check
     * @return {@code true} if the string is {@code null} or blank, {@code false} otherwise
     */
    public static boolean isNullOrWhiteSpace(String value) {
        return value == null || value.isBlank();
    }

    /**
     * Determines if the given string can be parsed as a {@code long} value.
     *
     * @param value the string to check
     * @return {@code true} if the string can be parsed as a {@code long}, {@code false} otherwise
     */
    public static boolean isParseableAsLong(String value) {
        try {
            Long.parseLong(value);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    /**
     * Determines if the given string can be parsed as a {@code double} value.
     *
     * @param value the string to check
     * @return {@code true} if the string can be parsed as a {@code double}, {@code false} otherwise
     */
    public static boolean isParseableAsDouble(String value) {
        try {
            Double.parseDouble(value);
        } catch (NullPointerException | NumberFormatException e) {
            return false;
        }

        return true;
    }

    /**
     * Determines if the given string can be interpreted as a boolean value.
     * Accepts "true", "false" (case-insensitive), "1", or "0".
     *
     * @param value the string to check
     * @return {@code true} if the string is a valid boolean representation, {@code false} otherwise
     */
    public static boolean isParseableAsBoolean(String value) {
        return "true".equalsIgnoreCase(value)
            || "false".equalsIgnoreCase(value)
            || "1".equals(value)
            || "0".equals(value);
    }

    public static Long convertToLong(String stringValue) throws YadConverterException {
        try {
            return Long.parseLong(stringValue);
        } catch (NumberFormatException ex) {
            throw new YadConverterException("Could not convert %s to LONG.", stringValue);
        }
    }

    public static Double convertToDouble(String stringValue) throws YadConverterException {
        try {
            return Double.parseDouble(stringValue);
        } catch (NullPointerException | NumberFormatException ex) {
            throw new YadConverterException("Could not convert %s to DOUBLE.", stringValue);
        }
    }

    public static Boolean convertToBoolean(String stringValue) throws YadConverterException {
        if ("true".equalsIgnoreCase(stringValue) || "1".equals(stringValue)) {
            return true;
        }

        if ("false".equalsIgnoreCase(stringValue) || "0".equals(stringValue)) {
            return false;
        }

        throw new YadConverterException("Could not convert %s to BOOLEAN.", stringValue);
    }

}
