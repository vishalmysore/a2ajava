package io.github.vishalmysore.debug;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A debugger utility for introspecting A2A classes and objects
 */
public class A2ADebugger {
    
    /**
     * Gets all methods of a class including inherited ones
     * @param clazz The class to inspect
     * @return List of method names
     */
    public List<String> getAllMethods(Class<?> clazz) {
        List<String> methodNames = new ArrayList<>();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            methodNames.add(method.getName());
        }
        return methodNames;
    }
    
    /**
     * Gets all fields of a class including inherited ones
     * @param clazz The class to inspect
     * @return List of field names
     */
    public List<String> getAllFields(Class<?> clazz) {
        List<String> fieldNames = new ArrayList<>();
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            fieldNames.add(field.getName());
        }
        return fieldNames;
    }
    
    /**
     * Gets declared methods of a class (excluding inherited ones)
     * @param clazz The class to inspect
     * @return List of method names
     */
    public List<String> getDeclaredMethods(Class<?> clazz) {
        List<String> methodNames = new ArrayList<>();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            methodNames.add(method.getName());
        }
        return methodNames;
    }
    
    /**
     * Gets declared fields of a class (excluding inherited ones)
     * @param clazz The class to inspect
     * @return List of field names
     */
    public List<String> getDeclaredFields(Class<?> clazz) {
        List<String> fieldNames = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            fieldNames.add(field.getName());
        }
        return fieldNames;
    }
    
    /**
     * Gets a particular method from a class
     * @param clazz The class to inspect
     * @param methodName The name of the method to find
     * @return Method object or null if not found
     */
    public Method getMethod(Class<?> clazz, String methodName) {
        try {
            return Arrays.stream(clazz.getMethods())
                    .filter(method -> method.getName().equals(methodName))
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Gets a particular field from a class
     * @param clazz The class to inspect
     * @param fieldName The name of the field to find
     * @return Field object or null if not found
     */
    public Field getField(Class<?> clazz, String fieldName) {
        try {
            return Arrays.stream(clazz.getFields())
                    .filter(field -> field.getName().equals(fieldName))
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            return null;
        }
    }
}