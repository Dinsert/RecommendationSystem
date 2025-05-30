package pro.sky.recommendation.system.controller.JUnitTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.recommendation.system.controller.DynamicRuleController;
import pro.sky.recommendation.system.entity.DynamicRule;
import pro.sky.recommendation.system.service.DynamicRuleService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DynamicRuleControllerJUnitTest {

    @Mock
    private DynamicRuleService dynamicRuleService;

    @InjectMocks
    private DynamicRuleController dynamicRuleController;

    private DynamicRule testRule;
    private UUID testRuleId;

    @BeforeEach
    void setUp() {
        testRuleId = UUID.randomUUID();
        testRule = new DynamicRule();
        testRule.setId(testRuleId);
        testRule.setProductName("Test Product");
        testRule.setProductText("Test Description");
    }

    @Test
    void createRule_shouldReturnCreatedRule() {
        // Arrange
        when(dynamicRuleService.createRule(any(DynamicRule.class))).thenReturn(testRule);

        // Act
        DynamicRule response = dynamicRuleController.createRule(testRule);

        // Assert
        assertNotNull(response);
        assertEquals(testRule, response);
        verify(dynamicRuleService, times(1)).createRule(testRule);
    }

    @Test
    void getAllRules_shouldReturnListOfRules() {
        // Arrange
        List<DynamicRule> rules = Arrays.asList(testRule);
        when(dynamicRuleService.getAllRules()).thenReturn(rules);

        // Act
        Map<String, List<DynamicRule>> response = dynamicRuleController.getAllRules();

        // Assert
        assertNotNull(response);
        assertNotNull(response);
        assertEquals(1, response.get("data").size());
        assertEquals(testRule, response.get("data").get(0));
        verify(dynamicRuleService, times(1)).getAllRules();
    }

    @Test
    void deleteRule_shouldCallServiceDelete() {
        // Arrange
        doNothing().when(dynamicRuleService).deleteRule(testRuleId);

        // Act
        dynamicRuleController.deleteRule(testRuleId);

        // Assert
        verify(dynamicRuleService, times(1)).deleteRule(testRuleId);
    }

    @Test
    void deleteRule_whenServiceThrowsException_shouldRethrow() {
        // Arrange
        doThrow(new RuntimeException("Test exception")).when(dynamicRuleService).deleteRule(testRuleId);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            dynamicRuleController.deleteRule(testRuleId);
        });
    }
}
