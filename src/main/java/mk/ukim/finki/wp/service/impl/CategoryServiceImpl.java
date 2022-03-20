package mk.ukim.finki.wp.service.impl;

import mk.ukim.finki.wp.model.Category;
import mk.ukim.finki.wp.repository.CategoryRepository;
import mk.ukim.finki.wp.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> listAll() {
        return this.categoryRepository.findAll();
    }

    @Override
    public Optional<Category> findById(Long id) {
        return this.categoryRepository.findById(id);
    }

    @Override
    public Optional<Category> findByName(String categoryName) {
        return this.categoryRepository.findByName(categoryName);
    }
}
