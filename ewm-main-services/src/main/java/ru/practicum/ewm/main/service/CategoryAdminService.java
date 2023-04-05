package ru.practicum.ewm.main.service;

import ru.practicum.ewm.main.dto.CategoryDto;
import ru.practicum.ewm.main.dto.CategoryDtoRequest;

public interface CategoryAdminService {
    CategoryDto addNewCategory(CategoryDtoRequest categoryDtoFromRequest);

    CategoryDto updateCategory(CategoryDtoRequest categoryDtoRequest, Long id);

    void deleteCategory(Long id);
}
