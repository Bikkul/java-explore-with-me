package ru.practicum.ewm.main.mapper;

import org.springframework.lang.NonNull;
import ru.practicum.ewm.main.dto.CategoryDto;
import ru.practicum.ewm.main.dto.CategoryDtoRequest;
import ru.practicum.ewm.main.model.Category;

import java.util.Optional;

public class CategoryDtoMapper {
    public static Category toCategory(@NonNull CategoryDtoRequest categoryDtoRequest) {
        Category category = new Category();

        Optional.ofNullable(categoryDtoRequest.getName()).ifPresent(category::setName);
        return category;
    }

    public static CategoryDto toCategoryDto(@NonNull Category category) {
        CategoryDto categoryDto = new CategoryDto();

        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        return categoryDto;
    }
}
