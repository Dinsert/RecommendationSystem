package pro.sky.recommendation.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pro.sky.recommendation.system.entity.DynamicRule;
import pro.sky.recommendation.system.service.DynamicRuleService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Контроллер для управления динамическими правилами.
 */
@RestController
@RequestMapping("/rule")
public class DynamicRuleController {
    /**
     * Сервис для операций над динамическими правилами.
     */
    private final DynamicRuleService dynamicRuleService;

    @Autowired
    public DynamicRuleController(DynamicRuleService dynamicRuleService) {
        this.dynamicRuleService = dynamicRuleService;
    }

    /**
     * Метод для создания нового правила.
     *
     * @param rule объект правила, передаваемый в теле запроса
     * @return созданный объект правила с HTTP-статусом OK
     */
    @PostMapping("/createRule")
    public DynamicRule createRule(@RequestBody DynamicRule rule) {
        return dynamicRuleService.createRule(rule);
    }

    /**
     * Метод для получения всех существующих правил.
     *
     * @return список всех правил, упакованный в карту с ключом "data" и HTTP-статус OK
     */
    @GetMapping("/getAllRules")
    public Map<String, List<DynamicRule>> getAllRules() {
        return Map.of("data", dynamicRuleService.getAllRules());
    }

    /**
     * Метод для удаления существующего правила по идентификатору.
     *
     * @param id уникальный идентификатор удаляемого правила.
     *           Возвращает успешный статус HTTP No Content
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/deleteRule/{id}")
    public void deleteRule(@PathVariable UUID id) {
        dynamicRuleService.deleteRule(id);
    }
}

