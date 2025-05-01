package regression;

import com.t4a.detect.ActionCallback;
import com.t4a.detect.ActionState;
import com.t4a.processor.AIProcessingException;
import com.t4a.processor.GeminiV2ActionProcessor;
import com.t4a.transform.GeminiV2PromptTransformer;
import com.t4a.transform.PromptTransformer;
import lombok.extern.java.Log;
import regression.pojo.Customer;
import regression.pojo.Organization;

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

        processor.processSingleAction("what does vishal like to eat",callback);
        String promptText = "Shahrukh Khan works for MovieHits inc and his salary is $ 100  he joined Toronto on Labor day, his tasks are acting and dancing. He also works out of Montreal and Bombay.Hrithik roshan is another employee of same company based in Chennai his taks are jumping and Gym he joined on Indian Independce Day";
        PromptTransformer promptTransformer = new GeminiV2PromptTransformer();
        log.info(promptTransformer.transformIntoPojo(promptText, Organization.class).toString());
        promptText = "Customer has a problem with his Computer create customer support ticket for him";
        log.info(promptTransformer.transformIntoPojo(promptText, Customer.class).toString());

        log.info((String) processor.processSingleAction(promptText));
    }
}
