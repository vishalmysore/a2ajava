package io.github.vishalmysore.a2ui;

import com.t4a.detect.ActionCallback;
import io.github.vishalmysore.common.CallBackType;

import java.util.*;

/**
 * Utility interface for creating A2UI display components
 * Provides reusable methods for building A2UI surface updates, components, and rendering messages
 */
public interface A2UIDisplay {

    /**
     * Creates a surface update map with the given surface ID
     * @param surfaceId The ID of the surface
     * @return Map containing surfaceId
     */
    default Map<String, Object> createSurfaceUpdate(String surfaceId) {
        Map<String, Object> surfaceUpdate = new HashMap<>();
        surfaceUpdate.put("surfaceId", surfaceId);
        return surfaceUpdate;
    }

    /**
     * Creates a root column component with child IDs
     * @param rootId The ID of the root component
     * @param childIds List of child component IDs
     * @return Map representing the root column component
     */
    default Map<String, Object> createRootColumn(String rootId, List<String> childIds) {
        Map<String, Object> root = new HashMap<>();
        root.put("id", rootId);

        Map<String, Object> rootComponent = new HashMap<>();
        Map<String, Object> columnProps = new HashMap<>();
        columnProps.put("children", new HashMap<String, Object>() {{
            put("explicitList", childIds);
        }});
        rootComponent.put("Column", columnProps);
        root.put("component", rootComponent);

        return root;
    }

    /**
     * Creates a text component
     * @param id The component ID
     * @param text The text content
     * @return Map representing the text component
     */
    default Map<String, Object> createTextComponent(String id, String text) {
        return createTextComponent(id, text, null);
    }

    /**
     * Creates a text component with usage hint
     * @param id The component ID
     * @param text The text content
     * @param usageHint Optional usage hint (e.g., "h1", "h2", "body")
     * @return Map representing the text component
     */
    default Map<String, Object> createTextComponent(String id, String text, String usageHint) {
        Map<String, Object> component = new HashMap<>();
        component.put("id", id);

        Map<String, Object> textComponent = new HashMap<>();
        Map<String, Object> textProps = new HashMap<>();
        textProps.put("text", new HashMap<String, Object>() {{
            put("literalString", text);
        }});

        if (usageHint != null && !usageHint.isEmpty()) {
            textProps.put("usageHint", usageHint);
        }

        textComponent.put("Text", textProps);
        component.put("component", textComponent);

        return component;
    }

    /**
     * Creates a data model update
     * @param surfaceId The surface ID
     * @return Map representing the data model update
     */
    default Map<String, Object> createDataModelUpdate(String surfaceId) {
        Map<String, Object> dataModelUpdate = new HashMap<>();
        dataModelUpdate.put("surfaceId", surfaceId);
        dataModelUpdate.put("contents", new ArrayList<>());
        return dataModelUpdate;
    }

    /**
     * Creates a data model update with initial values using adjacency list format (A2UI v0.8 spec)
     * @param surfaceId The surface ID
     * @param initialValues Map of data paths to initial values (e.g., "/form/name" -> "")
     * @return Map representing the data model update
     */
    default Map<String, Object> createDataModelUpdateWithValues(String surfaceId, Map<String, Object> initialValues) {
        Map<String, Object> dataModelUpdate = new HashMap<>();
        dataModelUpdate.put("surfaceId", surfaceId);

        List<Map<String, Object>> contents = new ArrayList<>();
        if (initialValues != null && !initialValues.isEmpty()) {
            // Group paths by their root key (e.g., "/form/name" -> root key is "form")
            Map<String, List<Map.Entry<String, Object>>> groupedByRoot = new LinkedHashMap<>();

            for (Map.Entry<String, Object> entry : initialValues.entrySet()) {
                String path = entry.getKey();
                // Parse path: "/form/name" -> root="form", subKey="name"
                String[] parts = path.split("/");
                if (parts.length >= 2) {
                    String rootKey = parts[1]; // Skip empty string from leading /
                    groupedByRoot.computeIfAbsent(rootKey, k -> new ArrayList<>()).add(entry);
                }
            }

            // Build adjacency list format
            for (Map.Entry<String, List<Map.Entry<String, Object>>> rootEntry : groupedByRoot.entrySet()) {
                Map<String, Object> rootItem = new HashMap<>();
                rootItem.put("key", rootEntry.getKey());

                List<Map<String, Object>> valueMap = new ArrayList<>();
                for (Map.Entry<String, Object> valueEntry : rootEntry.getValue()) {
                    String fullPath = valueEntry.getKey();
                    String[] pathParts = fullPath.split("/");

                    // Handle nested paths like "/reservation/menu/appetizer"
                    if (pathParts.length == 3) {
                        // Simple path: /form/name -> key="name"
                        Map<String, Object> valueItem = new HashMap<>();
                        valueItem.put("key", pathParts[2]);
                        valueItem.put("valueString", valueEntry.getValue());
                        valueMap.add(valueItem);
                    } else if (pathParts.length > 3) {
                        // Nested path: /reservation/menu/appetizer
                        // Create nested structure
                        String subKey = pathParts[2];
                        String leafKey = String.join("/", Arrays.copyOfRange(pathParts, 3, pathParts.length));

                        Map<String, Object> valueItem = new HashMap<>();
                        valueItem.put("key", subKey + "/" + leafKey);
                        valueItem.put("valueString", valueEntry.getValue());
                        valueMap.add(valueItem);
                    }
                }

                rootItem.put("valueMap", valueMap);
                contents.add(rootItem);
            }
        }
        dataModelUpdate.put("contents", contents);
        return dataModelUpdate;
    }

    /**
     * Creates a begin rendering message
     * @param surfaceId The surface ID
     * @param rootId The root component ID
     * @return Map representing the begin rendering message
     */
    default Map<String, Object> createBeginRendering(String surfaceId, String rootId) {
        Map<String, Object> beginRendering = new HashMap<>();
        beginRendering.put("surfaceId", surfaceId);
        beginRendering.put("root", rootId);
        return beginRendering;
    }

    /**
     * Creates a TextField component for user input with data model binding (A2UI v0.8 spec compliant)
     * @param id The component ID
     * @param label The label for the input field
     * @param dataPath The path in the data model (e.g., "/form/name")
     * @return Map representing the TextField component
     */
    default Map<String, Object> createTextFieldComponent(String id, String label, String dataPath) {
        Map<String, Object> component = new HashMap<>();
        component.put("id", id);

        Map<String, Object> textFieldComponent = new HashMap<>();
        Map<String, Object> textFieldProps = new HashMap<>();

        // Label is required for TextField
        textFieldProps.put("label", new HashMap<String, Object>() {{
            put("literalString", label);
        }});

        // Bind text to data model path (A2UI v0.8 uses 'text' property for TextField)
        textFieldProps.put("text", new HashMap<String, Object>() {{
            put("path", dataPath);
        }});

        textFieldComponent.put("TextField", textFieldProps);
        component.put("component", textFieldComponent);

        return component;
    }

    /**
     * Creates a Button component with action and context (A2UI v0.8 spec compliant)
     * @param id The component ID
     * @param buttonText The text to display on the button
     * @param actionName The action name to trigger
     * @param contextBindings Map of parameter names to data paths (e.g., "personName" -> "/form/name")
     * @return Map representing the Button component
     */
    default Map<String, Object> createButtonComponent(String id, String buttonText, String actionName, Map<String, String> contextBindings) {
        Map<String, Object> component = new HashMap<>();
        component.put("id", id);

        Map<String, Object> buttonComponent = new HashMap<>();
        Map<String, Object> buttonProps = new HashMap<>();

        // Button requires a child component (typically Text) and an action
        String childTextId = id + "_text";
        buttonProps.put("child", childTextId);

        // Action with context for data binding
        Map<String, Object> action = new HashMap<>();
        action.put("name", actionName);

        // Add context array if bindings are provided
        if (contextBindings != null && !contextBindings.isEmpty()) {
            List<Map<String, Object>> context = new ArrayList<>();
            for (Map.Entry<String, String> binding : contextBindings.entrySet()) {
                Map<String, Object> contextItem = new HashMap<>();
                contextItem.put("key", binding.getKey());
                contextItem.put("value", new HashMap<String, Object>() {{
                    put("path", binding.getValue());
                }});
                context.add(contextItem);
            }
            action.put("context", context);
        }

        buttonProps.put("action", action);

        buttonComponent.put("Button", buttonProps);
        component.put("component", buttonComponent);

        return component;
    }

    /**
     * Creates a Button component with action (no context) - for simple buttons
     * @param id The component ID
     * @param buttonText The text to display on the button
     * @param actionName The action name to trigger
     * @return Map representing the Button component
     */
    default Map<String, Object> createButtonComponent(String id, String buttonText, String actionName) {
        return createButtonComponent(id, buttonText, actionName, null);
    }

    /**
     * Creates a Text component for button child
     * @param id The component ID
     * @param text The text content
     * @return Map representing the Text component
     */
    default Map<String, Object> createButtonTextChild(String id, String text) {
        return createTextComponent(id, text);
    }

    /**
     * Builds a complete A2UI message with surface update, data model update, and begin rendering
     * @param surfaceId The surface ID
     * @param rootId The root component ID
     * @param components List of all components including root
     * @return Complete A2UI message map
     */
    default Map<String, Object> buildA2UIMessage(String surfaceId, String rootId, List<Map<String, Object>> components) {
        Map<String, Object> messages = new LinkedHashMap<>();

        // 1. Surface update with components
        Map<String, Object> surfaceUpdate = createSurfaceUpdate(surfaceId);
        surfaceUpdate.put("components", components);
        messages.put("surfaceUpdate", surfaceUpdate);

        // 2. Data model update
        messages.put("dataModelUpdate", createDataModelUpdate(surfaceId));

        // 3. Begin rendering
        messages.put("beginRendering", createBeginRendering(surfaceId, rootId));

        return messages;
    }

    /**
     * Builds a complete A2UI message with surface update, data model update with initial values, and begin rendering
     * @param surfaceId The surface ID
     * @param rootId The root component ID
     * @param components List of all components including root
     * @param dataModelValues Map of data paths to initial values
     * @return Complete A2UI message map
     */
    default Map<String, Object> buildA2UIMessageWithData(String surfaceId, String rootId,
                                                         List<Map<String, Object>> components,
                                                         Map<String, Object> dataModelValues) {
        Map<String, Object> messages = new LinkedHashMap<>();

        // 1. Surface update with components
        Map<String, Object> surfaceUpdate = createSurfaceUpdate(surfaceId);
        surfaceUpdate.put("components", components);
        messages.put("surfaceUpdate", surfaceUpdate);

        // 2. Data model update with initial values
        messages.put("dataModelUpdate", createDataModelUpdateWithValues(surfaceId, dataModelValues));

        // 3. Begin rendering
        messages.put("beginRendering", createBeginRendering(surfaceId, rootId));

        return messages;
    }
    default boolean isUICallback(ThreadLocal<ActionCallback> callback) {
        return callback != null && callback.get()!=null && callback.get().getType().equals(CallBackType.A2UI.name());
    }
}
