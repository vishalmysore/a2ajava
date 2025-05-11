package io.github.vishalmysore.a2a.server;

import com.t4a.annotations.Prompt;
import lombok.*;

/**
 * This will be send to AI to decide the type of the file to be proceessed and decide
 * what to do
 */
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FileProcessingInfo {
    @Prompt(describe = "What type of processing will be required for the content? your options are only one of these Selenium,Non-Selenium or FileIO")
    String typeOfProcessingRequired;
    @Prompt(describe = "check each line? can each line in this  be parallel processed? your options are only one of these true or false")
    boolean parallelProcessingAllowed;
}
