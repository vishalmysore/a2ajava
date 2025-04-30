package regression;

import com.t4a.detect.ActionCallback;
import com.t4a.detect.ActionState;
import com.t4a.processor.AIProcessingException;
import com.t4a.processor.GeminiV2ActionProcessor;
import io.github.vishalmysore.example.SimpleAction;
import lombok.extern.java.Log;

import javax.swing.*;

@Log
public class A2AStandalone {
    public static void main(String[] args) throws AIProcessingException {
        GeminiV2ActionProcessor processor = new GeminiV2ActionProcessor();
        ActionCallback callback = new ActionCallback() {

            @Override
            public void setContext(Object obj) {

            }

            @Override
            public Object getContext() {
                return null;
            }

            @Override
            public void sendtStatus(String status, ActionState state) {

            }


        };
        SimpleAction action = new SimpleAction();
        processor.processSingleAction("what does vishal like to eat",callback);
    }
}
