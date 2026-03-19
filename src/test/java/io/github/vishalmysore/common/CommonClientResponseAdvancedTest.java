package io.github.vishalmysore.common;

import io.github.vishalmysore.a2a.domain.*;
import io.github.vishalmysore.mcp.domain.CallToolResult;
import io.github.vishalmysore.mcp.domain.Content;
import io.github.vishalmysore.mcp.domain.TextContent;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Advanced unit tests for CommonClientResponse#getTextResult() covering
 * polymorphic dispatch for CallToolResult, SendTaskResponse, Task, and
 * various edge/boundary conditions.
 */
public class CommonClientResponseAdvancedTest {

    // ── CallToolResult branch ────────────────────────────────────────

    @Test
    void callToolResult_withSingleTextContent_shouldReturnText() {
        CallToolResult result = new CallToolResult();
        TextContent tc = new TextContent();
        tc.setText("hello from tool");
        result.setContent(new ArrayList<>(List.of(tc)));

        assertEquals("hello from tool", result.getTextResult());
    }

    @Test
    void callToolResult_withListOfTextContent_shouldReturnFirstText() {
        CallToolResult result = new CallToolResult();
        List<Content> list = new ArrayList<>();
        TextContent tc1 = new TextContent();
        tc1.setText("first");
        TextContent tc2 = new TextContent();
        tc2.setText("second");
        list.add(tc1);
        list.add(tc2);
        result.setContent(list);

        assertEquals("first", result.getTextResult());
    }

    @Test
    void callToolResult_withEmptyList_shouldReturnEmpty() {
        CallToolResult result = new CallToolResult();
        result.setContent(new ArrayList<>());

        assertEquals("", result.getTextResult());
    }

    @Test
    void callToolResult_withNullContent_shouldReturnEmpty() {
        CallToolResult result = new CallToolResult();
        result.setContent(null);

        assertEquals("", result.getTextResult());
    }

    // ── SendTaskResponse branch ──────────────────────────────────────

    @Test
    void sendTaskResponse_withTextPart_shouldReturnLastPartText() {
        SendTaskResponse response = new SendTaskResponse();
        Task task = new Task();
        TaskStatus status = new TaskStatus(TaskState.COMPLETED);
        Message msg = new Message();
        msg.setRole("agent");

        TextPart tp1 = new TextPart();
        tp1.setText("first part");
        TextPart tp2 = new TextPart();
        tp2.setText("last part");
        msg.setParts(new ArrayList<>(List.of(tp1, tp2)));
        status.setMessage(msg);
        task.setStatus(status);
        response.setResult(task);

        assertEquals("last part", response.getTextResult());
    }

    @Test
    void sendTaskResponse_withNullStatus_shouldReturnEmpty() {
        SendTaskResponse response = new SendTaskResponse();
        Task task = new Task();
        task.setStatus(null);
        response.setResult(task);

        assertEquals("", response.getTextResult());
    }

    @Test
    void sendTaskResponse_withNullMessage_shouldReturnEmpty() {
        SendTaskResponse response = new SendTaskResponse();
        Task task = new Task();
        TaskStatus status = new TaskStatus(TaskState.COMPLETED);
        status.setMessage(null);
        task.setStatus(status);
        response.setResult(task);

        assertEquals("", response.getTextResult());
    }

    @Test
    void sendTaskResponse_withEmptyParts_shouldReturnEmpty() {
        SendTaskResponse response = new SendTaskResponse();
        Task task = new Task();
        TaskStatus status = new TaskStatus(TaskState.COMPLETED);
        Message msg = new Message();
        msg.setParts(new ArrayList<>());
        status.setMessage(msg);
        task.setStatus(status);
        response.setResult(task);

        assertEquals("", response.getTextResult());
    }

    @Test
    void sendTaskResponse_withNullParts_shouldReturnEmpty() {
        SendTaskResponse response = new SendTaskResponse();
        Task task = new Task();
        TaskStatus status = new TaskStatus(TaskState.COMPLETED);
        Message msg = new Message();
        msg.setParts(null);
        status.setMessage(msg);
        task.setStatus(status);
        response.setResult(task);

        assertEquals("", response.getTextResult());
    }

    @Test
    void sendTaskResponse_lastPartIsDataPart_shouldReturnEmpty() {
        SendTaskResponse response = new SendTaskResponse();
        Task task = new Task();
        TaskStatus status = new TaskStatus(TaskState.COMPLETED);
        Message msg = new Message();
        msg.setRole("agent");

        TextPart tp = new TextPart();
        tp.setText("some text");
        DataPart dp = new DataPart();
        dp.setData(java.util.Map.of("key", "value"));
        msg.setParts(new ArrayList<>(List.of(tp, dp)));
        status.setMessage(msg);
        task.setStatus(status);
        response.setResult(task);

        // Last part is DataPart, not TextPart, so should return ""
        assertEquals("", response.getTextResult());
    }

    // ── Task branch (implements CommonClientResponse) ────────────────

    @Test
    void task_withTextPart_shouldReturnLastPartText() {
        Task task = new Task();
        TaskStatus status = new TaskStatus(TaskState.WORKING);
        Message msg = new Message();
        msg.setRole("agent");
        TextPart tp = new TextPart();
        tp.setText("task result");
        msg.setParts(new ArrayList<>(List.of(tp)));
        status.setMessage(msg);
        task.setStatus(status);

        assertEquals("task result", task.getTextResult());
    }

    @Test
    void task_withNullStatus_shouldReturnEmpty() {
        Task task = new Task();
        task.setStatus(null);
        assertEquals("", task.getTextResult());
    }

    @Test
    void task_withNullMessage_shouldReturnEmpty() {
        Task task = new Task();
        TaskStatus status = new TaskStatus(TaskState.WORKING);
        status.setMessage(null);
        task.setStatus(status);
        assertEquals("", task.getTextResult());
    }

    @Test
    void task_withNullParts_shouldReturnEmpty() {
        Task task = new Task();
        TaskStatus status = new TaskStatus(TaskState.WORKING);
        Message msg = new Message();
        msg.setParts(null);
        status.setMessage(msg);
        task.setStatus(status);
        assertEquals("", task.getTextResult());
    }

    @Test
    void task_withEmptyParts_shouldReturnEmpty() {
        Task task = new Task();
        TaskStatus status = new TaskStatus(TaskState.WORKING);
        Message msg = new Message();
        msg.setParts(new ArrayList<>());
        status.setMessage(msg);
        task.setStatus(status);
        assertEquals("", task.getTextResult());
    }

    @Test
    void task_multiplePartsReturnsLast() {
        Task task = new Task();
        TaskStatus status = new TaskStatus(TaskState.COMPLETED);
        Message msg = new Message();
        TextPart tp1 = new TextPart();
        tp1.setText("first");
        TextPart tp2 = new TextPart();
        tp2.setText("middle");
        TextPart tp3 = new TextPart();
        tp3.setText("last");
        msg.setParts(new ArrayList<>(List.of(tp1, tp2, tp3)));
        status.setMessage(msg);
        task.setStatus(status);

        assertEquals("last", task.getTextResult());
    }

    @Test
    void task_lastPartIsDataPart_shouldReturnEmpty() {
        Task task = new Task();
        TaskStatus status = new TaskStatus(TaskState.COMPLETED);
        Message msg = new Message();
        TextPart tp = new TextPart();
        tp.setText("text");
        DataPart dp = new DataPart();
        dp.setData(java.util.Map.of("k", "v"));
        msg.setParts(new ArrayList<>(List.of(tp, dp)));
        status.setMessage(msg);
        task.setStatus(status);

        assertEquals("", task.getTextResult());
    }

    // ── Unsupported type branch ──────────────────────────────────────

    @Test
    void unknownType_shouldThrowIllegalStateException() {
        // Create an anonymous implementation that is none of the expected types
        CommonClientResponse unknown = new CommonClientResponse() {};

        IllegalStateException ex = assertThrows(IllegalStateException.class, unknown::getTextResult);
        assertTrue(ex.getMessage().contains("Unexpected response type"));
    }

    // ── Special characters in text content ───────────────────────────

    @Test
    void callToolResult_textWithSpecialChars_shouldReturnExactly() {
        CallToolResult result = new CallToolResult();
        TextContent tc = new TextContent();
        String special = "Line1\nLine2\tTab<b>bold</b>&amp;\"quote\"";
        tc.setText(special);
        result.setContent(new ArrayList<>(List.of(tc)));

        assertEquals(special, result.getTextResult());
    }

    @Test
    void task_textWithUnicode_shouldReturnExactly() {
        Task task = new Task();
        TaskStatus status = new TaskStatus(TaskState.COMPLETED);
        Message msg = new Message();
        TextPart tp = new TextPart();
        tp.setText("\u00E9\u00E8\u00EA \u4F60\u597D \uD83D\uDE00");
        msg.setParts(new ArrayList<>(List.of(tp)));
        status.setMessage(msg);
        task.setStatus(status);

        assertEquals("\u00E9\u00E8\u00EA \u4F60\u597D \uD83D\uDE00", task.getTextResult());
    }
}
