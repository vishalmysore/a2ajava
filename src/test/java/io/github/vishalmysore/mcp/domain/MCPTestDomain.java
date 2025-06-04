package io.github.vishalmysore.mcp.domain;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.vishalmysore.mcp.client.MCPAgent;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class MCPTestDomain {

    private ObjectMapper mapper = new ObjectMapper();


    @Test
    public void testCallRequest() {
        try {
            String jsonString = "{\"method\":\"tools/call\",\"params\":{\"name\":\"browseWebAndPerformAction\",\"arguments\":{\"provideAllValuesInPlainEnglish\":\"Go to Google.com, search for \\\"a2ajava\\\", wait for results to load, click on the first search result link, wait for the page to load completely, then take a screenshot of the page\"}}}";
            CallToolRequest request = mapper.readValue(jsonString, CallToolRequest.class);
            assertNotNull(request);
            assertEquals("tools/call", request.getMethod());
            assertEquals("browseWebAndPerformAction", request.getParams().getName());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to parse CallToolRequest: " + e.getMessage(), e);
        }
    }

    @Test
    public void testCallToolResult() {
        try {
            // Create a sample CallToolResult
            CallToolResult result = new CallToolResult();
            List<Content> contentList = new ArrayList<>();
              // Add a text content
            TextContent textContent = new TextContent();
            textContent.setText("Test result message");
            textContent.setType("text");
            contentList.add(textContent);
            
            // Add an image content
            ImageContent imageContent = new ImageContent();
            imageContent.setMimeType("image/png");
            imageContent.setData("base64EncodedData");
            contentList.add(imageContent);
            
            result.setContent(contentList);

            String jsonString = mapper.writeValueAsString(result);
            CallToolResult parsedResult = mapper.readValue(jsonString, CallToolResult.class);
            
            assertNotNull(parsedResult);
            assertNotNull(parsedResult.getContent());
            assertEquals(2, parsedResult.getContent().size());
            
            // Verify text content
            Content firstContent = parsedResult.getContent().get(0);
            assertEquals(TextContent.class, firstContent.getClass());
            assertEquals("text", ((TextContent)firstContent).getType());
            assertEquals("Test result message", ((TextContent)firstContent).getText());
            
            // Verify image content
            Content secondContent = parsedResult.getContent().get(1);
            assertEquals(ImageContent.class, secondContent.getClass());
            assertEquals("image/png", ((ImageContent)secondContent).getMimeType());
            assertEquals("base64EncodedData", ((ImageContent)secondContent).getData());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to test CallToolResult: " + e.getMessage(), e);
        }
    }

    @Test
    public void testJsonRpcResponse() {
        try {
            JSONRPCResponse response = new JSONRPCResponse();
            response.setId("test-id-123");
            
            CallToolResult result = new CallToolResult();
            List<Content> contentList = new ArrayList<>();            TextContent textContent = new TextContent();
            textContent.setText("Response message");
            textContent.setType("text");
            contentList.add(textContent);
            result.setContent(contentList);
            
            response.setResult(result);

            String jsonString = mapper.writeValueAsString(response);
            JSONRPCResponse parsedResponse = mapper.readValue(jsonString, JSONRPCResponse.class);
            
            assertNotNull(parsedResponse);
            assertEquals("test-id-123", parsedResponse.getId());
            assertEquals("2.0", parsedResponse.getJsonrpc());
            
            assertNotNull(parsedResponse.getResult());
            assertTrue(parsedResponse.getResult() instanceof CallToolResult);
            CallToolResult parsedResult = (CallToolResult) parsedResponse.getResult();
            assertNotNull(parsedResult.getContent());
            assertEquals(1, parsedResult.getContent().size());
            
            Content content = parsedResult.getContent().get(0);
            assertEquals(TextContent.class, content.getClass());
            assertEquals("text", ((TextContent)content).getType());
            assertEquals("Response message", ((TextContent)content).getText());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to test JSONRPCResponse: " + e.getMessage(), e);
        }
    }

    @Test
    public void testAudioContent() {
        try {
            AudioContent audioContent = new AudioContent();
            audioContent.setData("base64EncodedAudioData");
            audioContent.setMimeType("audio/mp3");
            
            Annotations annotations = new Annotations();
            annotations.setPriority(1.0);
            annotations.setAudience(Arrays.asList(Role.USER));
            audioContent.setAnnotations(annotations);

            String jsonString = mapper.writeValueAsString(audioContent);
            AudioContent parsedContent = mapper.readValue(jsonString, AudioContent.class);
            
            assertNotNull(parsedContent);
            assertEquals("audio", parsedContent.getType());
            assertEquals("audio/mp3", parsedContent.getMimeType());
            assertEquals("base64EncodedAudioData", parsedContent.getData());
            assertNotNull(parsedContent.getAnnotations());
            assertEquals(1.0, parsedContent.getAnnotations().getPriority());
        } catch (Exception e) {
            throw new RuntimeException("Failed to test AudioContent: " + e.getMessage(), e);
        }
    }

    @Test
    public void testBlobAndTextResourceContents() {
        try {
            // Test BlobResourceContents
            BlobResourceContents blobContent = new BlobResourceContents();
            blobContent.setBlob("base64EncodedData");
            blobContent.setMimeType("application/octet-stream");
            blobContent.setUri("file:///example/blob.dat");

            String blobJson = mapper.writeValueAsString(blobContent);
            BlobResourceContents parsedBlob = mapper.readValue(blobJson, BlobResourceContents.class);
            
            assertNotNull(parsedBlob);
            assertEquals("base64EncodedData", parsedBlob.getBlob());
            assertEquals("application/octet-stream", parsedBlob.getMimeType());
            assertEquals("file:///example/blob.dat", parsedBlob.getUri());

            // Test TextResourceContents
            TextResourceContents textContent = new TextResourceContents();
            textContent.setText("Sample text content");
            textContent.setMimeType("text/plain");
            
            Annotations textAnnotations = new Annotations();
            textAnnotations.setPriority(2.0);
            textContent.setAnnotations(textAnnotations);

            String textJson = mapper.writeValueAsString(textContent);
            TextResourceContents parsedText = mapper.readValue(textJson, TextResourceContents.class);
            
            assertNotNull(parsedText);
            assertEquals("Sample text content", parsedText.getText());
            assertEquals("text/plain", parsedText.getMimeType());
            assertEquals(2.0, parsedText.getAnnotations().getPriority());
        } catch (Exception e) {
            throw new RuntimeException("Failed to test Resource Contents: " + e.getMessage(), e);
        }
    }

    @Test
    public void testServerCapabilities() {
        try {
            ServerCapabilities capabilities = new ServerCapabilities();
            capabilities.setContentTypes(Arrays.asList("text/plain", "image/png"));
            capabilities.setRoles(Arrays.asList("user", "assistant"));
            capabilities.setSamplingMethods(Arrays.asList("temperature", "top_p"));
            capabilities.setToolTypes(Arrays.asList("function", "command"));

            String jsonString = mapper.writeValueAsString(capabilities);
            ServerCapabilities parsedCapabilities = mapper.readValue(jsonString, ServerCapabilities.class);
            
            assertNotNull(parsedCapabilities);
            assertEquals(2, parsedCapabilities.getContentTypes().size());
            assertEquals(2, parsedCapabilities.getRoles().size());
            assertEquals(2, parsedCapabilities.getSamplingMethods().size());
            assertEquals(2, parsedCapabilities.getToolTypes().size());
            assertTrue(parsedCapabilities.getContentTypes().contains("text/plain"));
            assertTrue(parsedCapabilities.getToolTypes().contains("function"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to test ServerCapabilities: " + e.getMessage(), e);
        }
    }

    @Test
    public void testImplementationAndPreferences() {
        try {
            Implementation impl = new Implementation();
            impl.setName("TestServer");
            impl.setVersion("1.0.0");

            String implJson = mapper.writeValueAsString(impl);
            Implementation parsedImpl = mapper.readValue(implJson, Implementation.class);
            
            assertNotNull(parsedImpl);
            assertEquals("TestServer", parsedImpl.getName());
            assertEquals("1.0.0", parsedImpl.getVersion());

            ModelPreferences prefs = new ModelPreferences();
            prefs.setModelName("gpt-4");

            String prefsJson = mapper.writeValueAsString(prefs);
            ModelPreferences parsedPrefs = mapper.readValue(prefsJson, ModelPreferences.class);
            
            assertNotNull(parsedPrefs);
            assertEquals("gpt-4", parsedPrefs.getModelName());
        } catch (Exception e) {
            throw new RuntimeException("Failed to test Implementation/Preferences: " + e.getMessage(), e);
        }
    }

    @Test
    public void testSamplingMessage() {
        try {
            SamplingMessage message = new SamplingMessage();            message.setRole(Role.USER);
            
            TextContent textContent = new TextContent();
            textContent.setText("Sample message content");
            textContent.setType("text");
            message.setContent(textContent);

            String jsonString = mapper.writeValueAsString(message);
            SamplingMessage parsedMessage = mapper.readValue(jsonString, SamplingMessage.class);
            
            assertNotNull(parsedMessage);
            assertEquals(Role.USER, parsedMessage.getRole());
            assertNotNull(parsedMessage.getContent());
            assertTrue(parsedMessage.getContent() instanceof TextContent);
            assertEquals("Sample message content", ((TextContent)parsedMessage.getContent()).getText());
            assertEquals("text", ((TextContent)parsedMessage.getContent()).getType());
        } catch (Exception e) {
            throw new RuntimeException("Failed to test SamplingMessage: " + e.getMessage(), e);
        }
    }

    @Test
    public void testCancelNotification() {
        try {
            CancelledNotification cancelledNotification = new CancelledNotification();
           // cancelledNotification.setId("test-id-123");
            Map<String, Object> meta = new HashMap<>();
            meta.put("reason", "user_request");
         //   cancelledNotification.getParams().setMeta(meta);

            String jsonString = mapper.writeValueAsString(cancelledNotification);
            CancelledNotification parsedNotification = mapper.readValue(jsonString, CancelledNotification.class);

            assertNotNull(parsedNotification);
            assertEquals("notifications/cancelled", parsedNotification.getMethod());
           // assertEquals("test-id-123", parsedNotification.getId());
          //  assertNotNull(parsedNotification.getParams().getMeta());
          //  assertEquals("user_request", parsedNotification.getParams().getMeta().get("reason"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to test CancelledNotification: " + e.getMessage(), e);
        }
    }

    @Test
    public void testClientNotification() {
        try {
            InitializedNotification notification = new InitializedNotification();
            InitializedNotification.Params params = new InitializedNotification.Params();
            Map<String, Object> meta = new HashMap<>();
            meta.put("clientVersion", "1.0.0");
            params.setMeta(meta);
            notification.setParams(params);

            String jsonString = mapper.writeValueAsString(notification);
            InitializedNotification parsedNotification = mapper.readValue(jsonString, InitializedNotification.class);

            assertNotNull(parsedNotification);
            assertEquals("notifications/initialized", parsedNotification.getMethod());
            assertNotNull(parsedNotification.getParams());
            assertNotNull(parsedNotification.getParams().getMeta());
            assertEquals("1.0.0", parsedNotification.getParams().getMeta().get("clientVersion"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to test ClientNotification: " + e.getMessage(), e);
        }
    }

    @Test
    public void testCompleteResult() {
        try {
            CompleteResult result = new CompleteResult();
            Map<String, Object> meta = new HashMap<>();
            meta.put("source", "test");
            result.setMeta(meta);

            CompleteResult.Completion completion = new CompleteResult.Completion();
            completion.setHasMore(true);
            completion.setTotal(5);
            completion.setValues(Arrays.asList("completion1", "completion2"));
            result.setCompletion(completion);

            String jsonString = mapper.writeValueAsString(result);
            CompleteResult parsedResult = mapper.readValue(jsonString, CompleteResult.class);

            assertNotNull(parsedResult);
            assertEquals("test", parsedResult.getMeta().get("source"));
            assertTrue(parsedResult.getCompletion().getHasMore());
            assertEquals(Integer.valueOf(5), parsedResult.getCompletion().getTotal());
            assertEquals(2, parsedResult.getCompletion().getValues().size());
            assertEquals("completion1", parsedResult.getCompletion().getValues().get(0));
        } catch (Exception e) {
            throw new RuntimeException("Failed to test CompleteResult: " + e.getMessage(), e);
        }
    }

    @Test
    public void testCreateMessageRequest() {
        try {
            CreateMessageRequest request = new CreateMessageRequest();
            CreateMessageRequest.Params params = new CreateMessageRequest.Params();
            params.setIncludeContext("all");
            params.setMaxTokens(100);
            
            List<SamplingMessage> messages = new ArrayList<>();
            SamplingMessage message = new SamplingMessage();
            message.setRole(Role.USER);
            TextContent content = new TextContent();
            content.setText("Test message");
            content.setType("text");
            message.setContent(content);
            messages.add(message);
            params.setMessages(messages);
            
            ModelPreferences prefs = new ModelPreferences();
            prefs.setModelName("gpt-4");
            params.setModelPreferences(prefs);
            
            params.setStopSequences(Arrays.asList("stop1", "stop2"));
            request.setParams(params);

            String jsonString = mapper.writeValueAsString(request);
            CreateMessageRequest parsedRequest = mapper.readValue(jsonString, CreateMessageRequest.class);

            assertNotNull(parsedRequest);
            assertEquals("sampling/createMessage", parsedRequest.getMethod());
            assertEquals("all", parsedRequest.getParams().getIncludeContext());
            assertEquals(Integer.valueOf(100), parsedRequest.getParams().getMaxTokens());
            assertEquals(1, parsedRequest.getParams().getMessages().size());
            assertEquals(2, parsedRequest.getParams().getStopSequences().size());
            assertEquals("gpt-4", parsedRequest.getParams().getModelPreferences().getModelName());
        } catch (Exception e) {
            throw new RuntimeException("Failed to test CreateMessageRequest: " + e.getMessage(), e);
        }
    }

    @Test
    public void testCreateMessageResult() {
        try {
            CreateMessageResult result = new CreateMessageResult();
            Map<String, Object> meta = new HashMap<>();
            meta.put("temperature", 0.7);
            result.setMeta(meta);
            
            TextContent content = new TextContent();
            content.setText("Generated response");
            content.setType("text");
            result.setContent(content);
            
            result.setModel("gpt-4");
            result.setRole(Role.ASSISTANT);
            result.setStopReason("length");

            String jsonString = mapper.writeValueAsString(result);
            CreateMessageResult parsedResult = mapper.readValue(jsonString, CreateMessageResult.class);

            assertNotNull(parsedResult);
            assertEquals(0.7, parsedResult.getMeta().get("temperature"));
            assertTrue(parsedResult.getContent() instanceof TextContent);
            assertEquals("Generated response", ((TextContent)parsedResult.getContent()).getText());
            assertEquals("gpt-4", parsedResult.getModel());
            assertEquals(Role.ASSISTANT, parsedResult.getRole());
            assertEquals("length", parsedResult.getStopReason());
        } catch (Exception e) {
            throw new RuntimeException("Failed to test CreateMessageResult: " + e.getMessage(), e);
        }
    }

    @Test
    public void testEmbeddedResource() {
        try {
            EmbeddedResource resource = new EmbeddedResource();
            
            Annotations annotations = new Annotations();
            annotations.setPriority(1.0);
            resource.setAnnotations(annotations);
            
            TextResourceContents textResource = new TextResourceContents();
            textResource.setText("Resource content");
            textResource.setMimeType("text/plain");
            resource.setResource(textResource);

            String jsonString = mapper.writeValueAsString(resource);
            EmbeddedResource parsedResource = mapper.readValue(jsonString, EmbeddedResource.class);

            assertNotNull(parsedResource);
            assertEquals("resource", parsedResource.getType());
            assertEquals(1.0, parsedResource.getAnnotations().getPriority());
            assertTrue(parsedResource.getResource() instanceof TextResourceContents);
            TextResourceContents parsedTextResource = (TextResourceContents)parsedResource.getResource();
            assertEquals("Resource content", parsedTextResource.getText());
            assertEquals("text/plain", parsedTextResource.getMimeType());
        } catch (Exception e) {
            throw new RuntimeException("Failed to test EmbeddedResource: " + e.getMessage(), e);
        }
    }

    @Test
    public void testListToolsResult() {
        try {
            ListToolsResult result = new ListToolsResult();
            Map<String, Object> meta = new HashMap<>();
            meta.put("source", "test");
            result.setMeta(meta);
            
            List<Tool> tools = new ArrayList<>();            Tool tool = new Tool();
            tool.setName("testTool");
            tool.setDescription("A test tool");
            ToolParameters params = new ToolParameters();
            params.setRequired(Arrays.asList("param1", "param2"));
            tool.setParameters(params);
            tools.add(tool);
            result.setTools(tools);

            String jsonString = mapper.writeValueAsString(result);
            ListToolsResult parsedResult = mapper.readValue(jsonString, ListToolsResult.class);

            assertNotNull(parsedResult);
            assertEquals("test", parsedResult.getMeta().get("source"));
            assertEquals(1, parsedResult.getTools().size());
            Tool parsedTool = parsedResult.getTools().get(0);
            assertEquals("testTool", parsedTool.getName());
            assertEquals("A test tool", parsedTool.getDescription());
            assertNotNull(parsedTool.getParameters());
            assertEquals(2, parsedTool.getParameters().getRequired().size());
        } catch (Exception e) {
            throw new RuntimeException("Failed to test ListToolsResult: " + e.getMessage(), e);
        }
    }

    @Test
    public void testListResourcesResult() {
        try {
            ListResourcesResult result = new ListResourcesResult();
            Map<String, Object> meta = new HashMap<>();
            meta.put("total", 10);
            result.setMeta(meta);
            
            result.setNextCursor("next-page-token");
              List<Resource> resources = new ArrayList<>();
            Resource resource = new Resource();
            resource.setUri("file:///test.txt");
            TextResourceContents contents = new TextResourceContents();
            contents.setText("Test content");
            contents.setMimeType("text/plain");
            resource.setContents(contents);
            resources.add(resource);
            result.setResources(resources);

            String jsonString = mapper.writeValueAsString(result);
            ListResourcesResult parsedResult = mapper.readValue(jsonString, ListResourcesResult.class);

            assertNotNull(parsedResult);
            assertEquals(10, parsedResult.getMeta().get("total"));
            assertEquals("next-page-token", parsedResult.getNextCursor());
            assertEquals(1, parsedResult.getResources().size());
            Resource parsedResource = parsedResult.getResources().get(0);
            assertEquals("file:///test.txt", parsedResource.getUri());
            assertTrue(parsedResource.getContents() instanceof TextResourceContents);
            TextResourceContents parsedContents = (TextResourceContents) parsedResource.getContents();
            assertEquals("Test content", parsedContents.getText());
            assertEquals("text/plain", parsedContents.getMimeType());
        } catch (Exception e) {
            throw new RuntimeException("Failed to test ListResourcesResult: " + e.getMessage(), e);
        }
    }

    @Test
    public void testInitializeResult() {
        try {
            InitializeResult result = new InitializeResult();
            Map<String, Object> meta = new HashMap<>();
            meta.put("version", "1.0");
            result.setMeta(meta);
            
            ServerCapabilities capabilities = new ServerCapabilities();
            capabilities.setContentTypes(Arrays.asList("text", "image"));
            result.setCapabilities(capabilities);
            
            result.setInstructions("Test server instructions");
            result.setProtocolVersion("1.0.0");
            
            Implementation serverInfo = new Implementation();
            serverInfo.setName("TestServer");
            serverInfo.setVersion("2.0.0");
            result.setServerInfo(serverInfo);

            String jsonString = mapper.writeValueAsString(result);
            InitializeResult parsedResult = mapper.readValue(jsonString, InitializeResult.class);

            assertNotNull(parsedResult);
            assertEquals("1.0", parsedResult.getMeta().get("version"));
            assertNotNull(parsedResult.getCapabilities());
            assertEquals(2, parsedResult.getCapabilities().getContentTypes().size());
            assertEquals("Test server instructions", parsedResult.getInstructions());
            assertEquals("1.0.0", parsedResult.getProtocolVersion());
            assertEquals("TestServer", parsedResult.getServerInfo().getName());
            assertEquals("2.0.0", parsedResult.getServerInfo().getVersion());
        } catch (Exception e) {
            throw new RuntimeException("Failed to test InitializeResult: " + e.getMessage(), e);
        }
    }

    @Test
    public void testUnsubscribeRequest() {
        try {
            UnsubscribeRequest request = new UnsubscribeRequest();
            UnsubscribeRequest.Params params = new UnsubscribeRequest.Params();
            params.setUri("file:///test/resource.txt");
            request.setParams(params);

            String jsonString = mapper.writeValueAsString(request);
            UnsubscribeRequest parsedRequest = mapper.readValue(jsonString, UnsubscribeRequest.class);

            assertNotNull(parsedRequest);
            assertEquals("resources/unsubscribe", parsedRequest.getMethod());
            assertEquals("file:///test/resource.txt", parsedRequest.getParams().getUri());
        } catch (Exception e) {
            throw new RuntimeException("Failed to test UnsubscribeRequest: " + e.getMessage(), e);
        }
    }

    @Test
    public void testToolSchemas() {
        try {
            // Test ToolPropertySchema
            ToolPropertySchema propSchema = new ToolPropertySchema();
            propSchema.setType("string");
            propSchema.setDescription("A test property");
            Map<String, Object> additionalProps = new HashMap<>();
            additionalProps.put("format", "date-time");
            propSchema.setAdditionalProperties(additionalProps);
            propSchema.setItems(true);

            String propJson = mapper.writeValueAsString(propSchema);
            ToolPropertySchema parsedPropSchema = mapper.readValue(propJson, ToolPropertySchema.class);

            assertNotNull(parsedPropSchema);
            assertEquals("string", parsedPropSchema.getType());
            assertEquals("A test property", parsedPropSchema.getDescription());
            assertTrue(parsedPropSchema.isItems());
            assertEquals("date-time", parsedPropSchema.getAdditionalProperties().get("format"));

            // Test ToolInputSchema
            ToolInputSchema inputSchema = new ToolInputSchema();
            Map<String, ToolPropertySchema> properties = new HashMap<>();
            properties.put("testProp", propSchema);
            inputSchema.setProperties(properties);
            inputSchema.setRequired(Arrays.asList("testProp"));

            String inputJson = mapper.writeValueAsString(inputSchema);
            ToolInputSchema parsedInputSchema = mapper.readValue(inputJson, ToolInputSchema.class);

            assertNotNull(parsedInputSchema);
            assertEquals("object", parsedInputSchema.getType());
            assertEquals(1, parsedInputSchema.getProperties().size());
            assertEquals(1, parsedInputSchema.getRequired().size());
            assertEquals("testProp", parsedInputSchema.getRequired().get(0));
        } catch (Exception e) {
            throw new RuntimeException("Failed to test Tool Schemas: " + e.getMessage(), e);
        }
    }    @Test
    public void testResourceReference() {
        try {
            ResourceReference ref = new ResourceReference();
            ref.setUri("file:///test/ref.txt");

            String jsonString = mapper.writeValueAsString(ref);
            ResourceReference parsedRef = mapper.readValue(jsonString, ResourceReference.class);

            assertNotNull(parsedRef);
            assertEquals("file:///test/ref.txt", parsedRef.getUri());
        } catch (Exception e) {
            throw new RuntimeException("Failed to test ResourceReference: " + e.getMessage(), e);
        }
    }

    @Test
    public void testSubscribeRequest() {
        try {
            SubscribeRequest request = new SubscribeRequest();
            SubscribeRequest.Params params = new SubscribeRequest.Params();
            params.setUri("file:///test/resource.txt");
            request.setParams(params);

            String jsonString = mapper.writeValueAsString(request);
            SubscribeRequest parsedRequest = mapper.readValue(jsonString, SubscribeRequest.class);

            assertNotNull(parsedRequest);
            assertEquals("resources/subscribe", parsedRequest.getMethod());
            assertEquals("file:///test/resource.txt", parsedRequest.getParams().getUri());
        } catch (Exception e) {
            throw new RuntimeException("Failed to test SubscribeRequest: " + e.getMessage(), e);
        }
    }    @Test
    public void testSetLevelRequest() {
        try {
            SetLevelRequest request = new SetLevelRequest();
            SetLevelRequest.Params params = new SetLevelRequest.Params();
            params.setLevel("info");
            request.setParams(params);

            String jsonString = mapper.writeValueAsString(request);
            SetLevelRequest parsedRequest = mapper.readValue(jsonString, SetLevelRequest.class);

            assertNotNull(parsedRequest);
            assertEquals("server/setLevel", parsedRequest.getMethod());
            assertEquals("info", parsedRequest.getParams().getLevel());
        } catch (Exception e) {
            throw new RuntimeException("Failed to test SetLevelRequest: " + e.getMessage(), e);
        }
    }    @Test
    public void testSamplingContext() {
        try {
            SamplingContext context = new SamplingContext();
            context.setUri("file:///test/context.txt");
            
            List<PromptMessage> messages = new ArrayList<>();
            PromptMessage message = new PromptMessage();
            message.setRole(Role.USER);
            TextContent content = new TextContent();
            content.setText("Test context");
            content.setType("text");
            message.setContent(content);
            messages.add(message);
            context.setMessages(messages);

            String jsonString = mapper.writeValueAsString(context);
            SamplingContext parsedContext = mapper.readValue(jsonString, SamplingContext.class);

            assertNotNull(parsedContext);
            assertEquals("file:///test/context.txt", parsedContext.getUri());
            assertEquals(1, parsedContext.getMessages().size());
            PromptMessage parsedMessage = parsedContext.getMessages().get(0);
            assertEquals(Role.USER, parsedMessage.getRole());
            assertTrue(parsedMessage.getContent() instanceof TextContent);
            assertEquals("Test context", ((TextContent)parsedMessage.getContent()).getText());
        } catch (Exception e) {
            throw new RuntimeException("Failed to test SamplingContext: " + e.getMessage(), e);
        }
    }

    @Test
    public void testResourceTemplate() {
        try {
            ResourceTemplate template = new ResourceTemplate();
            template.setName("test-template");
            template.setUri("file:///templates/test.txt");
            
            Annotations annotations = new Annotations();
            annotations.setPriority(2.0);
            template.setAnnotations(annotations);

            String jsonString = mapper.writeValueAsString(template);
            ResourceTemplate parsedTemplate = mapper.readValue(jsonString, ResourceTemplate.class);

            assertNotNull(parsedTemplate);
            assertEquals("test-template", parsedTemplate.getName());
            assertEquals("file:///templates/test.txt", parsedTemplate.getUri());
            assertEquals(2.0, parsedTemplate.getAnnotations().getPriority());
        } catch (Exception e) {
            throw new RuntimeException("Failed to test ResourceTemplate: " + e.getMessage(), e);
        }
    }

    @Test
    public void testToolAnnotationsAndParameters() {
        try {
            // Test ToolAnnotations
            ToolAnnotations annotations = new ToolAnnotations();
            Map<String, Object> props = new HashMap<>();
            props.put("isAsync", true);
            props.put("timeout", 5000);
            annotations.setProperties(props);

            String annotationsJson = mapper.writeValueAsString(annotations);
            ToolAnnotations parsedAnnotations = mapper.readValue(annotationsJson, ToolAnnotations.class);

            assertNotNull(parsedAnnotations);
            assertTrue((Boolean) parsedAnnotations.getProperties().get("isAsync"));
            assertEquals(5000, parsedAnnotations.getProperties().get("timeout"));

            // Test ToolParameter
            ToolParameter param = new ToolParameter();
            param.setType("string");
            param.setDescription("A test parameter");

            String paramJson = mapper.writeValueAsString(param);
            ToolParameter parsedParam = mapper.readValue(paramJson, ToolParameter.class);

            assertNotNull(parsedParam);
            assertEquals("string", parsedParam.getType());
            assertEquals("A test parameter", parsedParam.getDescription());
        } catch (Exception e) {
            throw new RuntimeException("Failed to test Tool annotations and parameters: " + e.getMessage(), e);
        }
    }

    @Test
    public void testToolCallRequest() {
        try {
            ToolCallRequest request = new ToolCallRequest();
            request.setName("testTool");
            Map<String, Object> args = new HashMap<>();
            args.put("input", "test input");
            args.put("options", Arrays.asList("opt1", "opt2"));
            request.setArguments(args);

            String jsonString = mapper.writeValueAsString(request);
            ToolCallRequest parsedRequest = mapper.readValue(jsonString, ToolCallRequest.class);

            assertNotNull(parsedRequest);
            assertEquals("testTool", parsedRequest.getName());
            assertEquals("test input", parsedRequest.getArguments().get("input"));
            assertTrue(parsedRequest.getArguments().get("options") instanceof List);
            @SuppressWarnings("unchecked")
            List<String> options = (List<String>) parsedRequest.getArguments().get("options");
            assertEquals(2, options.size());
            assertEquals("opt1", options.get(0));
        } catch (Exception e) {
            throw new RuntimeException("Failed to test ToolCallRequest: " + e.getMessage(), e);
        }
    }

    @Test
    public void testToolParameterCombinations() {
        try {
            // Create tool parameter with all fields
            ToolParameter param = new ToolParameter();
            param.setType("object");
            param.setDescription("Complex parameter");

            // Test array type parameter
            String paramJson1 = "{\"type\":\"array\",\"description\":\"List parameter\"}";
            ToolParameter parsedParam1 = mapper.readValue(paramJson1, ToolParameter.class);
            assertEquals("array", parsedParam1.getType());
            assertEquals("List parameter", parsedParam1.getDescription());

            // Test number type parameter
            String paramJson2 = "{\"type\":\"number\",\"description\":\"Numeric parameter\"}";
            ToolParameter parsedParam2 = mapper.readValue(paramJson2, ToolParameter.class);
            assertEquals("number", parsedParam2.getType());
            assertEquals("Numeric parameter", parsedParam2.getDescription());

            // Test boolean type parameter
            String paramJson3 = "{\"type\":\"boolean\",\"description\":\"Flag parameter\"}";
            ToolParameter parsedParam3 = mapper.readValue(paramJson3, ToolParameter.class);
            assertEquals("boolean", parsedParam3.getType());
            assertEquals("Flag parameter", parsedParam3.getDescription());
        } catch (Exception e) {
            throw new RuntimeException("Failed to test Tool parameter combinations: " + e.getMessage(), e);
        }
    }

    @Test
    public void testToolAnnotationsEdgeCases() {
        try {
            ToolAnnotations annotations = new ToolAnnotations();
            
            // Test with empty map
            String jsonString1 = mapper.writeValueAsString(annotations);
            ToolAnnotations parsedAnnotations1 = mapper.readValue(jsonString1, ToolAnnotations.class);
            assertNotNull(parsedAnnotations1.getProperties());
            assertTrue(parsedAnnotations1.getProperties().isEmpty());

            // Test with null values
            Map<String, Object> props = new HashMap<>();
            props.put("nullValue", null);
            annotations.setProperties(props);
            String jsonString2 = mapper.writeValueAsString(annotations);
            ToolAnnotations parsedAnnotations2 = mapper.readValue(jsonString2, ToolAnnotations.class);
            assertTrue(parsedAnnotations2.getProperties().containsKey("nullValue"));
            assertNull(parsedAnnotations2.getProperties().get("nullValue"));

            // Test with nested objects
            Map<String, Object> nestedProps = new HashMap<>();
            Map<String, Object> nestedMap = new HashMap<>();
            nestedMap.put("nestedKey", "nestedValue");
            nestedProps.put("nested", nestedMap);
            annotations.setProperties(nestedProps);
            String jsonString3 = mapper.writeValueAsString(annotations);
            ToolAnnotations parsedAnnotations3 = mapper.readValue(jsonString3, ToolAnnotations.class);
            assertTrue(parsedAnnotations3.getProperties().get("nested") instanceof Map);
            @SuppressWarnings("unchecked")
            Map<String, Object> parsedNestedMap = (Map<String, Object>) parsedAnnotations3.getProperties().get("nested");
            assertEquals("nestedValue", parsedNestedMap.get("nestedKey"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to test Tool annotations edge cases: " + e.getMessage(), e);
        }
    }

    @Test
    public void testResourceTemplateEdgeCases() {
        try {
            ResourceTemplate template = new ResourceTemplate();
            
            // Test with minimum required fields
            String jsonString1 = mapper.writeValueAsString(template);
            ResourceTemplate parsedTemplate1 = mapper.readValue(jsonString1, ResourceTemplate.class);
            assertNotNull(parsedTemplate1);
            assertNull(parsedTemplate1.getName());
            assertNull(parsedTemplate1.getUri());
            assertNull(parsedTemplate1.getAnnotations());

            // Test with empty strings
            template.setName("");
            template.setUri("");
            String jsonString2 = mapper.writeValueAsString(template);
            ResourceTemplate parsedTemplate2 = mapper.readValue(jsonString2, ResourceTemplate.class);
            assertEquals("", parsedTemplate2.getName());
            assertEquals("", parsedTemplate2.getUri());

            // Test with special characters in name and URI
            template.setName("Test Template!@#$%^&*()");
            template.setUri("file:///path/with/spaces and ★special★ chars/template.txt");
            String jsonString3 = mapper.writeValueAsString(template);
            ResourceTemplate parsedTemplate3 = mapper.readValue(jsonString3, ResourceTemplate.class);
            assertEquals("Test Template!@#$%^&*()", parsedTemplate3.getName());
            assertEquals("file:///path/with/spaces and ★special★ chars/template.txt", parsedTemplate3.getUri());
        } catch (Exception e) {
            throw new RuntimeException("Failed to test Resource template edge cases: " + e.getMessage(), e);
        }
    }

    @Test
    public void testImplementation() {
        Implementation impl = new Implementation();
        impl.setName("TestServer");
        impl.setVersion("1.0.0");
        
        assertEquals("TestServer", impl.getName());
        assertEquals("1.0.0", impl.getVersion());
    }

    @Test
    public void testClientCapabilitiesConfig() {
        ClientCapabilities caps = new ClientCapabilities();
        
        // Test roots capabilities
        ClientCapabilities.Roots roots = new ClientCapabilities.Roots();
        roots.setListChanged(true);
        assertEquals(true, roots.getListChanged());
        
        Map<String, Object> sampling = new HashMap<>();
        sampling.put("method", "temperature");
        sampling.put("value", 0.7);
        caps.setSampling(sampling);
    }
    
    @Test
    public void testServerCapabilitiesConfig() {
        ServerCapabilities caps = new ServerCapabilities();
        
        List<String> contentTypes = Arrays.asList("text/plain", "application/json");
        caps.setContentTypes(contentTypes);
        assertEquals(contentTypes, caps.getContentTypes());
        
        List<String> roles = Arrays.asList("USER", "ASSISTANT", "SYSTEM");
        caps.setRoles(roles);
        assertEquals(roles, caps.getRoles());
        
        List<String> samplingMethods = Arrays.asList("temperature", "top_p");
        caps.setSamplingMethods(samplingMethods);
        assertEquals(samplingMethods, caps.getSamplingMethods());
    }
    
    @Test
    public void testMCPGenericResponse() {
        MCPGenericResponse<String> response = new MCPGenericResponse<>();
        response.setJsonrpc("2.0");
        response.setId("123");
        response.setResult("Test Result");
        
        assertEquals("2.0", response.getJsonrpc());
        assertEquals("123", response.getId());
        assertEquals("Test Result", response.getResult());
    }
    
    @Test
    public void testInitializedNotification() {
        // Test InitializedNotification as example
        InitializedNotification notification = new InitializedNotification();
        InitializedNotification.Params params = new InitializedNotification.Params();
        
        Map<String, Object> meta = new HashMap<>();
        meta.put("clientId", "test-client");
        meta.put("timestamp", System.currentTimeMillis());
        
        params.setMeta(meta);
        notification.setParams(params);
        
        assertEquals("notifications/initialized", notification.getMethod());
        assertEquals(meta, notification.getParams().getMeta());
    }
    
    @Test
    public void testMCPAgentSetup() throws MalformedURLException {
        URL serverUrl = new URL("http://localhost:8080");
        MCPAgent agent = new MCPAgent(serverUrl);
        
        // Test basic properties
        assertEquals("mcp", agent.getType());
        assertEquals(serverUrl, agent.getServerUrl());
        assertFalse(agent.isConnected());
        
        // Test object mapper configuration
        ObjectMapper mapper = agent.getMapper();
        assertFalse(mapper.isEnabled(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES));
    }
    
    // ...existing tests...
}
