package io.github.vishalmysore.a2ui;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class A2UIAwareTest {

    private final A2UIAware a2uiAware = new A2UIAware() {
        // Implement required methods from ActionCallbackAware if any
    };

    @Test
    public void testCreateSurfaceUpdate() {
        String surfaceId = "testSurface";
        Map<String, Object> result = a2uiAware.createSurfaceUpdate(surfaceId);

        assertNotNull(result);
        assertEquals(surfaceId, result.get("surfaceId"));
    }

    @Test
    public void testCreateRootColumn() {
        String rootId = "root1";
        List<String> childIds = Arrays.asList("child1", "child2");
        Map<String, Object> result = a2uiAware.createRootColumn(rootId, childIds);

        assertNotNull(result);
        assertEquals(rootId, result.get("id"));
        assertTrue(result.containsKey("component"));
        Map<String, Object> component = (Map<String, Object>) result.get("component");
        assertTrue(component.containsKey("Column"));
    }

    @Test
    public void testCreateTextComponentWithoutUsageHint() {
        String id = "text1";
        String text = "Hello World";
        Map<String, Object> result = a2uiAware.createTextComponent(id, text);

        assertNotNull(result);
        assertEquals(id, result.get("id"));
        assertTrue(result.containsKey("component"));
        Map<String, Object> component = (Map<String, Object>) result.get("component");
        assertTrue(component.containsKey("Text"));
    }

    @Test
    public void testCreateTextComponentWithUsageHint() {
        String id = "text2";
        String text = "Title";
        String usageHint = "h1";
        Map<String, Object> result = a2uiAware.createTextComponent(id, text, usageHint);

        assertNotNull(result);
        assertEquals(id, result.get("id"));
        Map<String, Object> component = (Map<String, Object>) result.get("component");
        Map<String, Object> textComponent = (Map<String, Object>) component.get("Text");
        assertEquals(usageHint, textComponent.get("usageHint"));
    }

    @Test
    public void testCreateDataModelUpdate() {
        String surfaceId = "dataSurface";
        Map<String, Object> result = a2uiAware.createDataModelUpdate(surfaceId);

        assertNotNull(result);
        assertEquals(surfaceId, result.get("surfaceId"));
        assertTrue(result.containsKey("contents"));
        assertTrue(result.get("contents") instanceof List);
    }

    @Test
    public void testCreateDataModelUpdateWithValues() {
        String surfaceId = "dataSurface";
        Map<String, Object> initialValues = new HashMap<>();
        initialValues.put("/form/name", "");
        initialValues.put("/form/email", "");

        Map<String, Object> result = a2uiAware.createDataModelUpdateWithValues(surfaceId, initialValues);

        assertNotNull(result);
        assertEquals(surfaceId, result.get("surfaceId"));
        assertTrue(result.containsKey("contents"));
        List<?> contents = (List<?>) result.get("contents");
        assertFalse(contents.isEmpty());
    }

    @Test
    public void testCreateBeginRendering() {
        String surfaceId = "renderSurface";
        String rootId = "root1";
        Map<String, Object> result = a2uiAware.createBeginRendering(surfaceId, rootId);

        assertNotNull(result);
        assertEquals(surfaceId, result.get("surfaceId"));
        assertEquals(rootId, result.get("root"));
    }

    @Test
    public void testCreateTextFieldComponent() {
        String id = "input1";
        String label = "Name";
        String dataPath = "/form/name";
        Map<String, Object> result = a2uiAware.createTextFieldComponent(id, label, dataPath);

        assertNotNull(result);
        assertEquals(id, result.get("id"));
        Map<String, Object> component = (Map<String, Object>) result.get("component");
        assertTrue(component.containsKey("TextField"));
    }

    @Test
    public void testCreateButtonComponent() {
        String id = "button1";
        String buttonText = "Click Me";
        String actionName = "submit";
        Map<String, Object> result = a2uiAware.createButtonComponent(id, buttonText, actionName);

        assertNotNull(result);
        assertEquals(id, result.get("id"));
        Map<String, Object> component = (Map<String, Object>) result.get("component");
        assertTrue(component.containsKey("Button"));
    }

    @Test
    public void testCreateButtonTextChild() {
        String id = "buttonText1";
        String text = "Submit";
        Map<String, Object> result = a2uiAware.createButtonTextChild(id, text);

        assertNotNull(result);
        assertEquals(id, result.get("id"));
        Map<String, Object> component = (Map<String, Object>) result.get("component");
        assertTrue(component.containsKey("Text"));
    }

    @Test
    public void testCreateImageComponent() {
        String id = "image1";
        String url = "http://example.com/image.png";
        String fit = "cover";
        String usageHint = "background";
        Map<String, Object> result = a2uiAware.createImageComponent(id, url, fit, usageHint);

        assertNotNull(result);
        assertEquals(id, result.get("id"));
        Map<String, Object> component = (Map<String, Object>) result.get("component");
        assertTrue(component.containsKey("Image"));
    }

    @Test
    public void testCreateCheckBoxComponent() {
        String id = "checkbox1";
        String label = "Agree";
        String dataPath = "/form/agree";
        Map<String, Object> result = a2uiAware.createCheckBoxComponent(id, label, dataPath);

        assertNotNull(result);
        assertEquals(id, result.get("id"));
        Map<String, Object> component = (Map<String, Object>) result.get("component");
        assertTrue(component.containsKey("CheckBox"));
    }

    @Test
    public void testCreateCardComponent() {
        String id = "card1";
        String childId = "child1";
        Map<String, Object> result = a2uiAware.createCardComponent(id, childId);

        assertNotNull(result);
        assertEquals(id, result.get("id"));
        Map<String, Object> component = (Map<String, Object>) result.get("component");
        assertTrue(component.containsKey("Card"));
    }

    @Test
    public void testCreateLoadingComponent() {
        String id = "loading1";
        String message = "Loading...";
        Map<String, Object> result = a2uiAware.createLoadingComponent(id, message);

        assertNotNull(result);
        assertEquals(id, result.get("id"));
        Map<String, Object> component = (Map<String, Object>) result.get("component");
        assertTrue(component.containsKey("Column"));
    }
}