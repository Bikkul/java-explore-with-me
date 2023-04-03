package ru.practicum.ewm.main.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.dto.CategoryDto;
import ru.practicum.ewm.main.service.CategoryPublicService;

import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/categories")
@Slf4j
@RequiredArgsConstructor
@Validated
public class PublicCategoriesController {
    private final CategoryPublicService categoryPublicService;

    @GetMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<CategoryDto> getCategories(@RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                           @RequestParam(required = false, defaultValue = "10") @PositiveOrZero Integer size) {
        List<CategoryDto> categories = categoryPublicService.getCategories(from, size);
        log.info("categories list with size={} has been got", categories.size());
        return categories;
    }

    @GetMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto getCategoryInfoById(@PathVariable Long catId) {
        CategoryDto category = categoryPublicService.getCategoryById(catId);
        log.info("category with fields { " +
                "id={}, " +
                "name={} " +
                "} has been got", category.getId(), category.getName());
        return category;
    }
}
