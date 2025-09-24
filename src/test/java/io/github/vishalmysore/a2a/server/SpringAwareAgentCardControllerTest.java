package io.github.vishalmysore.a2a.server;

import com.t4a.predict.PredictionLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class SpringAwareAgentCardControllerTest {

    @Mock
    private ApplicationContext context;
    
    @Mock
    private PredictionLoader predictionLoader;
    
    private SpringAwareAgentCardController controller;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    void testConstructor() {
        // Arrange - mock static method
        try (MockedStatic<PredictionLoader> mockedStatic = mockStatic(PredictionLoader.class)) {
            mockedStatic.when(() -> PredictionLoader.getInstance(any(ApplicationContext.class))).thenReturn(predictionLoader);
            
            // Act - create controller with mocked context
            controller = new SpringAwareAgentCardController(context);
            
            // Assert
            assertNotNull(controller);
            mockedStatic.verify(() -> PredictionLoader.getInstance(context), times(1));
        }
    }
}