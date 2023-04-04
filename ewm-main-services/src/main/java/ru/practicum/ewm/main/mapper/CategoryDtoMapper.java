package ru.practicum.ewm.main.mapper;

import org.springframework.lang.NonNull;
import ru.practicum.ewm.main.dto.CategoryDto;
import ru.practicum.ewm.main.dto.CategoryDtoRequest;
import ru.practicum.ewm.main.model.Category;

public class CategoryDtoMapper {
    private CategoryDtoMapper() {
    }

    public static Category toCategory(@NonNull CategoryDtoRequest categoryDtoRequest) {
        Category category = new Category();

        category.setName(categoryDtoRequest.getName());
        return category;
    }

    public static CategoryDto toCategoryDto(@NonNull Category category) {
        CategoryDto categoryDto = new CategoryDto();

        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        return categoryDto;
    }
}
