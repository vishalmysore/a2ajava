# Spring and Selenium Integration Guide

## Selenium Integration

Tools4AI's integration with Selenium introduces a flexible way to automate UI testing. Instead of traditional Java code for Selenium scripts, Tools4AI allows you to define test scenarios in plain English, offering a more accessible approach to testing web applications. These English-based commands can be converted into Selenium code to automate web-based interactions and streamline testing.

### Example of Selenium Test with Tools4AI

```java
WebDriver driver = new ChromeDriver(options);
SeleniumProcessor processor = new SeleniumProcessor(driver);
processor.processWebAction("go to website https://the-internet.herokuapp.com");
boolean buttonPresent = processor.trueFalseQuery("do you see Add/Remove Elements?");
if(buttonPresent) {
    processor.processWebAction("click on Add/Remove Elements");
    // perform other function in simple english
} 
//else {
//    processor.processSingleAction("Create Jira by taking screenshot");
//}

processor.processWebAction("go to website https://the-internet.herokuapp.com");
boolean isCheckboxPresent = processor.trueFalseQuery("do you see Checkboxes?");
if(isCheckboxPresent) {
    processor.processWebAction("click on Checkboxes");
    processor.processWebAction("select checkbox 1");
}
```

In this example, the SeleniumProcessor processes commands in plain English and converts them into Selenium actions. This approach allows for complex interactions without manually writing Java code for each test. Tools4AI serves as a bridge between natural language and Selenium, making it easier to automate UI testing in a way that is both efficient and intuitive.

This integration offers substantial benefits for teams looking to streamline their UI validation process. By enabling a more straightforward way to define and execute Selenium scripts, Tools4AI provides a flexible framework for automating Selenium-based tests.

## Spring Integration

All the action processors have Spring integration as well:

```java
SpringAnthropicProcessor springAnthropic = new SpringAnthropicProcessor(applicationContext);
SpringGeminiProcessor springGemini = new SpringGeminiProcessor();
SpringOpenAIProcessor springOpenAI = new SpringOpenAIProcessor();
```

You can use this for spring injection and it works exactly as all other action processors. The only difference is that instead of creating new action beans it will reuse the beans already created by spring.

### Example Implementation

Here are some example implementations showing how to use Spring with Tools4AI:

#### SAM Controller Example
```java
package io.github.vishalmysore;

import com.t4a.api.ActionGroup;
import com.t4a.api.GroupInfo;
import com.t4a.predict.GeminiPromptTransformer;
import com.t4a.predict.PredictionLoader;
import com.t4a.predict.PromptTransformer;
import com.t4a.processor.*;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.java.Log;
import io.github.vishalmysore.pojo.Customer;
import io.github.vishalmysore.pojo.RestaurantPojo;
import io.github.vishalmysore.service.RestaurantBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log
@RestController
public class SAMController {
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private RestaurantBookingService restaurantBookingService;

    @Operation(summary = "Execute any action based on prompt", 
              description = "Try out with any of these prompts:\n" +
                          "1) My Customer name is Vishal, his computer needs repair\n" +
                          "2) Can you compare Honda City to Toyota Corolla\n" +
                          "3) Can i Go out without Jacket in Toronto today\n" +
                          "4) what would vishal want to eat today?")
    public String actOnPrompt(@RequestParam("prompt") String prompt) {
        AIProcessor processor = new SpringGeminiProcessor(applicationContext);
        try {
            return (String) processor.processSingleAction(prompt);
        } catch (AIProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    // ... other controller methods ...
}

#### Service Examples

```java
@Service
@Log
@Predict(actionName ="compareCar", description = "Provide 2 cars and compare them")
public class CompareCarService implements JavaMethodAction {
    public CompareCarService() {
        log.info("created compare car service");
    }
    
    public String compareCar(String car1, String car2) {
        log.info(car2);
        log.info(car1);
        // implement the comparison logic here
        return "this is better - " + car2;
    }
}

@Log
@Service
public class RestaurantBookingService {
    public RestaurantBookingService() {
        log.info("created RestaurantBookingService");
    }
    
    public String bookReservation(RestaurantPojo restaurantPojo) {
        log.info(restaurantPojo.toString());
        return "This has been booked " + restaurantPojo.toString();
    }
}