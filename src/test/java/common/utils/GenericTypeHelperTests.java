package common.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenericTypeHelperTests {

    static class TestClass {
        public TestClass() {}
    }

    static class NoDefaultConstructor {
        public NoDefaultConstructor(String arg) {}
    }

    @Test
    void testConstructInstanceSuccess() {
        TestClass instance = GenericTypeHelper.constructInstance(TestClass.class);
        assertNotNull(instance);
        assertTrue(instance instanceof TestClass);
    }

    @Test
    void testConstructInstanceThrowsForNoDefaultConstructor() {
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
            GenericTypeHelper.constructInstance(NoDefaultConstructor.class)
        );
        assertTrue(ex.getMessage().contains("Error while creating an instance of type"));
    }
}