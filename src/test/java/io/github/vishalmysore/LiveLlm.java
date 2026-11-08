package io.github.vishalmysore;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Some tests need a real reply from an LLM. They are annotated with
 * {@code @EnabledIf("io.github.vishalmysore.LiveLlm#available")} so they are skipped, not failed,
 * when no OpenAI key is configured (for example in CI).
 * <p>
 * To run them, put a real key in {@code openAiKey} in src/test/resources/tools4ai.properties and clear
 * {@code openAiBaseURL} (it points at the langchain4j demo proxy, which no longer accepts the demo key).
 * The key is looked up the same way Tools4AI does: tools4ai.properties first, then -D, then env.
 */
public final class LiveLlm {

    private LiveLlm() {
    }

    public static boolean available() {
        String key = null;
        try (InputStream in = LiveLlm.class.getClassLoader().getResourceAsStream("tools4ai.properties")) {
            if (in != null) {
                Properties props = new Properties();
                props.load(in);
                key = props.getProperty("openAiKey");
            }
        } catch (IOException ignored) {
            // fall through to -D and env
        }
        if (isBlank(key)) {
            key = System.getProperty("openAiKey");
        }
        if (isBlank(key)) {
            key = System.getenv("openAiKey");
        }
        return !isBlank(key) && !"demo".equals(key.trim());
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
