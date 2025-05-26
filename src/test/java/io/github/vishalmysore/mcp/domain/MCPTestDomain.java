package io.github.vishalmysore.mcp.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

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
}
