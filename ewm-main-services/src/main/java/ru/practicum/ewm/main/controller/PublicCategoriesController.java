package ru.practicum.ewm.main.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.dto.CategoryDto;
import ru.practicum.ewm.main.services.CategoryPublicService;

import java.util.List;

@RestController("/categories")
@Slf4j
@RequiredArgsConstructor
public class PublicCategoriesController {
    private final CategoryPublicService categoryPublicService;

    @GetMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<CategoryDto> getCategories(@RequestParam Integer from,
                                           @RequestParam Integer size) {
        List<CategoryDto> categories = categoryPublicService.getCategories(from, size);
        log.info("categories list with size={} has been got", categories.size());
        return categories;
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategoryInfoById(@PathVariable Long catId) {
        CategoryDto category = categoryPublicService.getCategoryById(catId);
        log.info("category with fields { " +
                "id={}, " +
                "name={} " +
                "} has been got", category.getId(), category.getName());
        return category;
    }
}
