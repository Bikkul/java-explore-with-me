package ru.practicum.ewm.main.services;

import ru.practicum.ewm.main.dto.CategoryDto;

import java.util.List;

public interface CategoryPublicService {
    List<CategoryDto> getCategories(Integer from, Integer size);

    CategoryDto getCategoryById(Long id);
}
