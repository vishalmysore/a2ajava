package io.github.vishalmysore.a2ui;

import com.t4a.detect.ActionCallback;
import com.t4a.processor.ActionCallbackAware;
import io.github.vishalmysore.common.CallBackType;

import java.util.*;

/**
 * Utility interface for creating A2UI display components
 * Provides reusable methods for building A2UI surface updates, components, and rendering messages
 */
public interface A2UIAware extends ActionCallbackAware {

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
        Map<String, Object> childrenMap = new HashMap<>();
        childrenMap.put("explicitList", childIds);
        columnProps.put("children", childrenMap);
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
        Map<String, Object> textMap = new HashMap<>();
        textMap.put("literalString", text);
        textProps.put("text", textMap);

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
        Map<String, Object> labelMap = new HashMap<>();
        labelMap.put("literalString", label);
        textFieldProps.put("label", labelMap);

        // Bind text to data model path (A2UI v0.8 uses 'text' property for TextField)
        Map<String, Object> textMap = new HashMap<>();
        textMap.put("path", dataPath);
        textFieldProps.put("text", textMap);

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
                Map<String, Object> valueMap = new HashMap<>();
                valueMap.put("path", binding.getValue());
                contextItem.put("value", valueMap);
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
    default boolean isUICallback(ActionCallback callback) {
        return callback != null && callback.getType().equals(CallBackType.A2UI.name());
    }

    default boolean isUICallback() {
        return isUICallback(getCallback());
    }
    /**
     * Creates an Image component
     */
    default Map<String, Object> createImageComponent(String id, String url, String fit, String usageHint) {
        Map<String, Object> component = new HashMap<>();
        component.put("id", id);

        Map<String, Object> imageComponent = new HashMap<>();
        Map<String, Object> imageProps = new HashMap<>();

        Map<String, Object> urlMap = new HashMap<>();
        urlMap.put("literalString", url);
        imageProps.put("url", urlMap);

        if (fit != null) {
            imageProps.put("fit", fit);
        }
        if (usageHint != null) {
            imageProps.put("usageHint", usageHint);
        }

        imageComponent.put("Image", imageProps);
        component.put("component", imageComponent);

        return component;
    }

    /**
     * Creates a CheckBox component with data binding
     */
    default Map<String, Object> createCheckBoxComponent(String id, String label, String dataPath) {
        Map<String, Object> component = new HashMap<>();
        component.put("id", id);

        Map<String, Object> checkBoxComponent = new HashMap<>();
        Map<String, Object> checkBoxProps = new HashMap<>();

        Map<String, Object> labelMap = new HashMap<>();
        labelMap.put("literalString", label);
        checkBoxProps.put("label", labelMap);

        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put("path", dataPath);
        checkBoxProps.put("value", valueMap);

        checkBoxComponent.put("CheckBox", checkBoxProps);
        component.put("component", checkBoxComponent);

        return component;
    }

    /**
     * Creates a Slider component for numeric input
     */
    default Map<String, Object> createSliderComponent(String id, String label, String dataPath,
                                                      Double minValue, Double maxValue) {
        Map<String, Object> component = new HashMap<>();
        component.put("id", id);

        Map<String, Object> sliderComponent = new HashMap<>();
        Map<String, Object> sliderProps = new HashMap<>();

        Map<String, Object> labelMap = new HashMap<>();
        labelMap.put("literalString", label);
        sliderProps.put("label", labelMap);

        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put("path", dataPath);
        sliderProps.put("value", valueMap);

        if (minValue != null) {
            sliderProps.put("minValue", minValue);
        }
        if (maxValue != null) {
            sliderProps.put("maxValue", maxValue);
        }

        sliderComponent.put("Slider", sliderProps);
        component.put("component", sliderComponent);

        return component;
    }

    /**
     * Creates a Card container component
     */
    default Map<String, Object> createCardComponent(String id, String childId) {
        Map<String, Object> component = new HashMap<>();
        component.put("id", id);

        Map<String, Object> cardComponent = new HashMap<>();
        Map<String, Object> cardProps = new HashMap<>();

        cardProps.put("child", childId);

        cardComponent.put("Card", cardProps);
        component.put("component", cardComponent);

        return component;
    }

    /**
     * Creates a Row layout component
     */
    default Map<String, Object> createRowComponent(String id, List<String> childIds,
                                                   String alignment, String distribution) {
        Map<String, Object> component = new HashMap<>();
        component.put("id", id);

        Map<String, Object> rowComponent = new HashMap<>();
        Map<String, Object> rowProps = new HashMap<>();

        Map<String, Object> childrenMap = new HashMap<>();
        childrenMap.put("explicitList", childIds);
        rowProps.put("children", childrenMap);

        if (alignment != null) {
            rowProps.put("alignment", alignment);
        }
        if (distribution != null) {
            rowProps.put("distribution", distribution);
        }

        rowComponent.put("Row", rowProps);
        component.put("component", rowComponent);

        return component;
    }

    /**
     * Creates a form with multiple input fields
     */
    default Map<String, Object> createForm(String surfaceId, String title,
                                           List<Map<String, Object>> fields,
                                           String submitActionName) {
        List<String> childIds = new ArrayList<>();
        List<Map<String, Object>> components = new ArrayList<>();

        // Add title
        String titleId = "form_title";
        childIds.add(titleId);
        components.add(createTextComponent(titleId, title, "h2"));

        // Add fields
        for (Map<String, Object> field : fields) {
            childIds.add((String) field.get("id"));
            components.add(field);
        }

        // Add submit button
        String buttonId = "submit_button";
        String buttonTextId = "submit_button_text";
        childIds.add(buttonId);
        childIds.add(buttonTextId);

        components.add(createButtonTextChild(buttonTextId, "Submit"));
        components.add(createButtonComponent(buttonId, "Submit", submitActionName));

        // Add root column
        components.add(createRootColumn("root", childIds));

        return buildA2UIMessage(surfaceId, "root", components);
    }

    /**
     * Creates a data-bound List component with template
     */
    default Map<String, Object> createListComponent(String id, String dataPath, String templateComponentId) {
        Map<String, Object> component = new HashMap<>();
        component.put("id", id);

        Map<String, Object> listComponent = new HashMap<>();
        Map<String, Object> listProps = new HashMap<>();

        Map<String, Object> childrenMap = new HashMap<>();
        childrenMap.put("path", dataPath);
        childrenMap.put("componentId", templateComponentId);
        listProps.put("children", childrenMap);

        listComponent.put("List", listProps);
        component.put("component", listComponent);

        return component;
    }


    /**
     * Creates a loading indicator component
     */
    default Map<String, Object> createLoadingComponent(String id, String message) {
        Map<String, Object> component = new HashMap<>();
        component.put("id", id);

        Map<String, Object> columnComponent = new HashMap<>();
        Map<String, Object> columnProps = new HashMap<>();

        List<String> childIds = Arrays.asList("loading_icon", "loading_text");
        Map<String, Object> childrenMap = new HashMap<>();
        childrenMap.put("explicitList", childIds);
        columnProps.put("children", childrenMap);
        columnProps.put("alignment", "center");
        columnProps.put("distribution", "center");

        columnComponent.put("Column", columnProps);
        component.put("component", columnComponent);

        // This would be part of the components list, not the component itself
        // You would need to add the icon and text components separately

        return component;
    }

    /**
     * Creates an error message component
     */
    default Map<String, Object> createErrorComponent(String id, String errorMessage) {
        Map<String, Object> component = new HashMap<>();
        component.put("id", id);

        Map<String, Object> cardComponent = new HashMap<>();
        Map<String, Object> cardProps = new HashMap<>();

        // Create a card with error styling
        String errorContentId = id + "_content";
        cardProps.put("child", errorContentId);

        cardComponent.put("Card", cardProps);
        component.put("component", cardComponent);

        return component;
    }

    /**
     * Creates a divider component
     */
    default Map<String, Object> createDividerComponent(String id) {
        Map<String, Object> component = new HashMap<>();
        component.put("id", id);

        Map<String, Object> dividerComponent = new HashMap<>();
        dividerComponent.put("Divider", new HashMap<>());
        component.put("component", dividerComponent);

        return component;
    }

    /**
     * Creates a choice picker for single or multiple selections
     */
    default Map<String, Object> createChoicePickerComponent(String id, String label,
                                                            List<Map<String, String>> options,
                                                            String usageHint) {
        Map<String, Object> component = new HashMap<>();
        component.put("id", id);

        Map<String, Object> choiceComponent = new HashMap<>();
        Map<String, Object> choiceProps = new HashMap<>();

        Map<String, Object> labelMap = new HashMap<>();
        labelMap.put("literalString", label);
        choiceProps.put("label", labelMap);

        List<Map<String, Object>> optionList = new ArrayList<>();
        for (Map<String, String> option : options) {
            Map<String, Object> optionMap = new HashMap<>();
            optionMap.put("value", option.get("value"));
            Map<String, Object> optionLabelMap = new HashMap<>();
            optionLabelMap.put("literalString", option.get("label"));
            optionMap.put("label", optionLabelMap);
            optionList.add(optionMap);
        }
        choiceProps.put("options", optionList);

        if (usageHint != null) {
            choiceProps.put("usageHint", usageHint);
        }

        choiceComponent.put("ChoicePicker", choiceProps);
        component.put("component", choiceComponent);

        return component;
    }

    /**
     * Creates a Video component
     */
    default Map<String, Object> createVideoComponent(String id, String url, String usageHint) {
        Map<String, Object> component = new HashMap<>();
        component.put("id", id);

        Map<String, Object> videoComponent = new HashMap<>();
        Map<String, Object> videoProps = new HashMap<>();

        Map<String, Object> urlMap = new HashMap<>();
        urlMap.put("literalString", url);
        videoProps.put("url", urlMap);

        if (usageHint != null) {
            videoProps.put("usageHint", usageHint);
        }

        videoComponent.put("Video", videoProps);
        component.put("component", videoComponent);

        return component;
    }

    /**
     * Creates an AudioPlayer component
     */
    default Map<String, Object> createAudioPlayerComponent(String id, String url, String description) {
        Map<String, Object> component = new HashMap<>();
        component.put("id", id);

        Map<String, Object> audioComponent = new HashMap<>();
        Map<String, Object> audioProps = new HashMap<>();

        Map<String, Object> urlMap = new HashMap<>();
        urlMap.put("literalString", url);
        audioProps.put("url", urlMap);

        if (description != null) {
            Map<String, Object> descriptionMap = new HashMap<>();
            descriptionMap.put("literalString", description);
            audioProps.put("description", descriptionMap);
        }

        audioComponent.put("AudioPlayer", audioProps);
        component.put("component", audioComponent);

        return component;
    }

    /**
     * Creates a Tabs component
     */
    default Map<String, Object> createTabsComponent(String id, List<Map<String, Object>> tabItems) {
        Map<String, Object> component = new HashMap<>();
        component.put("id", id);

        Map<String, Object> tabsComponent = new HashMap<>();
        Map<String, Object> tabsProps = new HashMap<>();

        List<Map<String, Object>> items = new ArrayList<>();
        for (Map<String, Object> tab : tabItems) {
            Map<String, Object> itemMap = new HashMap<>();
            Map<String, Object> titleMap = new HashMap<>();
            titleMap.put("literalString", tab.get("title"));
            itemMap.put("title", titleMap);
            itemMap.put("child", tab.get("child"));
            items.add(itemMap);
        }
        tabsProps.put("tabItems", items);

        tabsComponent.put("Tabs", tabsProps);
        component.put("component", tabsComponent);

        return component;
    }

    /**
     * Creates a Modal component
     */
    default Map<String, Object> createModalComponent(String id, String entryPointChild, String contentChild) {
        Map<String, Object> component = new HashMap<>();
        component.put("id", id);

        Map<String, Object> modalComponent = new HashMap<>();
        Map<String, Object> modalProps = new HashMap<>();

        modalProps.put("entryPointChild", entryPointChild);
        modalProps.put("contentChild", contentChild);

        modalComponent.put("Modal", modalProps);
        component.put("component", modalComponent);

        return component;
    }

    /**
     * Creates a DateTimeInput component
     */
    default Map<String, Object> createDateTimeInputComponent(String id, String label, String dataPath,
                                                             Boolean enableDate, Boolean enableTime) {
        Map<String, Object> component = new HashMap<>();
        component.put("id", id);

        Map<String, Object> dateTimeComponent = new HashMap<>();
        Map<String, Object> dateTimeProps = new HashMap<>();

        Map<String, Object> labelMap = new HashMap<>();
        labelMap.put("literalString", label);
        dateTimeProps.put("label", labelMap);

        if (dataPath != null) {
            Map<String, Object> valueMap = new HashMap<>();
            valueMap.put("path", dataPath);
            dateTimeProps.put("value", valueMap);
        }

        if (enableDate != null) {
            dateTimeProps.put("enableDate", enableDate);
        }

        if (enableTime != null) {
            dateTimeProps.put("enableTime", enableTime);
        }

        dateTimeComponent.put("DateTimeInput", dateTimeProps);
        component.put("component", dateTimeComponent);

        return component;
    }
}
