package ru.practicum.ewm.main.mapper;

import ru.practicum.ewm.main.dto.CategoryDto;
import ru.practicum.ewm.main.dto.CategoryDtoRequest;
import ru.practicum.ewm.main.model.Category;

import java.util.Optional;

public class CategoryDtoMapper {
    public static Category toCategory(CategoryDtoRequest categoryDtoRequest) {
        Category category = new Category();

        Optional.ofNullable(categoryDtoRequest.getName()).ifPresent(category::setName);
        return category;
    }

    public static CategoryDto toCategoryDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();

        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        return categoryDto;
    }
}
