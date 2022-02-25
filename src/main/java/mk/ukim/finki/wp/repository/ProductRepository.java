package mk.ukim.finki.wp.repository;

import mk.ukim.finki.wp.model.Brand;
import mk.ukim.finki.wp.model.Category;
import mk.ukim.finki.wp.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAllByCategory(Category category, Pageable pageable);

    Page<Product> findAllByBrand(Brand brand, Pageable pageable);

    Page<Product> findAllByPriceBetween(Double price1, Double price2, Pageable pageable);

    Page<Product> findAllByNameLikeIgnoreCase(String name, Pageable pageable);
}
