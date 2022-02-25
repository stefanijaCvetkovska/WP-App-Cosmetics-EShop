package mk.ukim.finki.wp.service;

import mk.ukim.finki.wp.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> listAll();
    Optional<Category> findById(Long id);
}
