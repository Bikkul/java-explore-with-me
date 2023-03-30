package ru.practicum.ewm.main.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.dto.CategoryDto;
import ru.practicum.ewm.main.dto.CategoryDtoRequest;
import ru.practicum.ewm.main.services.CategoryAdminService;

import javax.validation.Valid;

@RestController("/admin/categories")
@Slf4j
@RequiredArgsConstructor
public class AdminCategoriesController {
    private final CategoryAdminService categoryAdminService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addNewCategory(@RequestBody @Valid CategoryDtoRequest categoryDtoRequest) {
        CategoryDto savedCategory = categoryAdminService.addNewCategory(categoryDtoRequest);
        log.info("category with field { " +
                "name={} " +
                "} has been saved", categoryDtoRequest.getName());
        return savedCategory;
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long catId) {
        categoryAdminService.deleteCategory(catId);
        log.info("category with id={} has been deleted", catId);
    }

    @PatchMapping("/{catId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public CategoryDto updateCategory(@PathVariable Long catId,
                                      @RequestBody @Valid CategoryDtoRequest categoryDtoRequest) {
        CategoryDto updatedCategory = categoryAdminService.updateCategory(categoryDtoRequest, catId);
        log.info("category with fields { " +
                "id={}," +
                "name={} " +
                "} has been updated", catId, categoryDtoRequest.getName());
        return updatedCategory;
    }
}
