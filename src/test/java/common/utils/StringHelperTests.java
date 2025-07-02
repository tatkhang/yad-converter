package common.utils;

import common.exceptions.YadConverterException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringHelperTests {

    @Test
    void testIsNullOrWhiteSpace() {
        assertTrue(StringHelper.isNullOrWhiteSpace(null));
        assertTrue(StringHelper.isNullOrWhiteSpace(""));
        assertTrue(StringHelper.isNullOrWhiteSpace("   "));
        assertFalse(StringHelper.isNullOrWhiteSpace("abc"));
    }

    @Test
    void testIsParseableAsLong() {
        assertTrue(StringHelper.isParseableAsLong("123"));
        assertTrue(StringHelper.isParseableAsLong("-456"));
        assertFalse(StringHelper.isParseableAsLong("12.3"));
        assertFalse(StringHelper.isParseableAsLong("abc"));
        assertFalse(StringHelper.isParseableAsLong(null));
    }

    @Test
    void testIsParseableAsDouble() {
        assertTrue(StringHelper.isParseableAsDouble("123"));
        assertTrue(StringHelper.isParseableAsDouble("-456.78"));
        assertTrue(StringHelper.isParseableAsDouble("0.0"));
        assertFalse(StringHelper.isParseableAsDouble("abc"));
        assertFalse(StringHelper.isParseableAsDouble(null));
    }

    @Test
    void testIsParseableAsBoolean() {
        assertTrue(StringHelper.isParseableAsBoolean("true"));
        assertTrue(StringHelper.isParseableAsBoolean("false"));
        assertTrue(StringHelper.isParseableAsBoolean("TRUE"));
        assertTrue(StringHelper.isParseableAsBoolean("0"));
        assertTrue(StringHelper.isParseableAsBoolean("1"));
        assertFalse(StringHelper.isParseableAsBoolean("yes"));
        assertFalse(StringHelper.isParseableAsBoolean("no"));
        assertFalse(StringHelper.isParseableAsBoolean("abc"));
        assertFalse(StringHelper.isParseableAsBoolean(null));
    }

    @Test
    void testConvertToLong() throws YadConverterException {
        assertEquals(123L, StringHelper.convertToLong("123"));
        assertEquals(-456L, StringHelper.convertToLong("-456"));
        YadConverterException ex = assertThrows(YadConverterException.class, () -> StringHelper.convertToLong("abc"));
        assertTrue(ex.getMessage().contains("Could not convert"));
    }

    @Test
    void testConvertToDouble() throws YadConverterException {
        assertEquals(123.0, StringHelper.convertToDouble("123"));
        assertEquals(-456.78, StringHelper.convertToDouble("-456.78"));
        YadConverterException ex = assertThrows(YadConverterException.class, () -> StringHelper.convertToDouble("abc"));
        assertTrue(ex.getMessage().contains("Could not convert"));
    }

    @Test
    void testConvertToBoolean() throws YadConverterException {
        assertTrue(StringHelper.convertToBoolean("true"));
        assertTrue(StringHelper.convertToBoolean("TRUE"));
        assertTrue(StringHelper.convertToBoolean("1"));
        assertFalse(StringHelper.convertToBoolean("false"));
        assertFalse(StringHelper.convertToBoolean("0"));
        YadConverterException ex = assertThrows(YadConverterException.class, () -> StringHelper.convertToBoolean("abc"));
        assertTrue(ex.getMessage().contains("Could not convert"));
    }

}