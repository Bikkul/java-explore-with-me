package ru.practicum.ewm.main.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.main.common.MyPageRequest;
import ru.practicum.ewm.main.dto.CategoryDto;
import ru.practicum.ewm.main.dto.CategoryDtoRequest;
import ru.practicum.ewm.main.exception.CategoryNotFoundException;
import ru.practicum.ewm.main.mapper.CategoryDtoMapper;
import ru.practicum.ewm.main.model.Category;
import ru.practicum.ewm.main.repository.CategoryRepository;
import ru.practicum.ewm.main.services.CategoryAdminService;
import ru.practicum.ewm.main.services.CategoryPublicService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryAdminService, CategoryPublicService {
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public CategoryDto addNewCategory(CategoryDtoRequest categoryDtoRequest) {
        Category category = CategoryDtoMapper.toCategory(categoryDtoRequest);
        Category savedCategory = categoryRepository.save(category);
        return CategoryDtoMapper.toCategoryDto(savedCategory);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(CategoryDtoRequest categoryDtoRequest, Long id) {
        checkCategoryExists(id);
        Category updatedCategory = categoryRepository.save(categoryToUpdate(categoryDtoRequest, id));
        return CategoryDtoMapper.toCategoryDto(updatedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        checkCategoryExists(id);
        categoryRepository.deleteById(id);
    }

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        return categoryRepository.findAll(MyPageRequest.of(from, size))
                .stream()
                .map(CategoryDtoMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        checkCategoryExists(id);
        return CategoryDtoMapper.toCategoryDto(categoryRepository.getReferenceById(id));
    }

    private void checkCategoryExists(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new CategoryNotFoundException(String.format("Category with id=%d was not found", id));
        }
    }

    private Category categoryToUpdate(CategoryDtoRequest categoryDtoRequest, Long id) {
        Category category = categoryRepository.getReferenceById(id);

        Optional.ofNullable(categoryDtoRequest.getName()).ifPresent(category::setName);
        return category;
    }
}
