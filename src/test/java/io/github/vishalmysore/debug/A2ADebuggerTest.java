package io.github.vishalmysore.debug;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for A2ADebugger
 */
public class A2ADebuggerTest {
    
    private A2ADebugger debugger;
    
    // Simple test class to use for reflection tests
    static class TestClass {
        public String publicField;
        private String privateField;
        protected String protectedField;
        
        public void publicMethod() {}
        private void privateMethod() {}
        protected void protectedMethod() {}
    }
    
    // Test subclass to verify inheritance
    static class TestSubclass extends TestClass {
        public String childPublicField;
        private String childPrivateField;
        
        public void childPublicMethod() {}
        private void childPrivateMethod() {}
    }
    
    @BeforeEach
    void setUp() {
        debugger = new A2ADebugger();
    }
    
    @Test
    void testGetAllMethods() {
        List<String> methods = debugger.getAllMethods(TestClass.class);
        
        assertTrue(methods.contains("publicMethod"), "Should contain public method");
        assertTrue(methods.contains("wait"), "Should contain inherited methods like wait()");
        assertTrue(methods.contains("equals"), "Should contain inherited methods like equals()");
        assertTrue(methods.contains("toString"), "Should contain inherited methods like toString()");
    }
    
    @Test
    void testGetAllMethodsWithInheritance() {
        List<String> methods = debugger.getAllMethods(TestSubclass.class);
        
        assertTrue(methods.contains("publicMethod"), "Should contain parent's public method");
        assertTrue(methods.contains("childPublicMethod"), "Should contain child's public method");
    }
    
    @Test
    void testGetAllFields() {
        List<String> fields = debugger.getAllFields(TestClass.class);
        
        assertTrue(fields.contains("publicField"), "Should contain public field");
        assertFalse(fields.contains("privateField"), "Should not contain private field");
    }
    
    @Test
    void testGetAllFieldsWithInheritance() {
        List<String> fields = debugger.getAllFields(TestSubclass.class);
        
        assertTrue(fields.contains("publicField"), "Should contain parent's public field");
        assertTrue(fields.contains("childPublicField"), "Should contain child's public field");
    }
    
    @Test
    void testGetDeclaredMethods() {
        List<String> methods = debugger.getDeclaredMethods(TestClass.class);
        
        assertTrue(methods.contains("publicMethod"), "Should contain public method");
        assertTrue(methods.contains("privateMethod"), "Should contain private method");
        assertTrue(methods.contains("protectedMethod"), "Should contain protected method");
        assertFalse(methods.contains("toString"), "Should not contain inherited methods");
    }
    
    @Test
    void testGetDeclaredFields() {
        List<String> fields = debugger.getDeclaredFields(TestClass.class);
        
        assertTrue(fields.contains("publicField"), "Should contain public field");
        assertTrue(fields.contains("privateField"), "Should contain private field");
        assertTrue(fields.contains("protectedField"), "Should contain protected field");
    }
    
    @Test
    void testGetMethod() {
        Method method = debugger.getMethod(TestClass.class, "publicMethod");
        
        assertNotNull(method, "Should find public method");
        assertEquals("publicMethod", method.getName(), "Method name should match");
    }
    
    @Test
    void testGetNonExistentMethod() {
        Method method = debugger.getMethod(TestClass.class, "nonExistentMethod");
        
        assertNull(method, "Should return null for non-existent method");
    }
    
    @Test
    void testGetField() {
        Field field = debugger.getField(TestClass.class, "publicField");
        
        assertNotNull(field, "Should find public field");
        assertEquals("publicField", field.getName(), "Field name should match");
    }
    
    @Test
    void testGetNonExistentField() {
        Field field = debugger.getField(TestClass.class, "nonExistentField");
        
        assertNull(field, "Should return null for non-existent field");
    }
}