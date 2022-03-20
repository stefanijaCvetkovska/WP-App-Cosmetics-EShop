package mk.ukim.finki.wp.service;

import mk.ukim.finki.wp.model.Brand;
import mk.ukim.finki.wp.model.Product;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> listAll();
    Optional<Product> findById(Long id);
    Product create(String name, Double price, Integer quantity, String description, Long brandId, Long categoryId, String image);
    Product update(Long id, String name, Double price, Integer quantity, String description, Long brandId, Long categoryId, String image);
    Product delete(Long id);

    Page<Product> listAllProducts(int pageNo, int pageSize, String sortField, String sortDirection);
    Page<Product> listProductsByCategory(int pageNo, int pageSize, String sortField, String sortDirection, Long categoryId);
    Page<Product> listProductsByBrand(int pageNo, int pageSize, String sortField, String sortDirection, Long brandId);
    Page<Product> listProductsByPriceBetween(int pageNo, int pageSize, String sortField, String sortDirection, Double price1,Double price2);
    Page<Product> listProductsByNameLike(int pageNo, int pageSize, String sortField, String sortDirection, String name);

    List<Product> listAllProductsByCategoryName(String categoryName);
    List<Product> listAllProductsByBrandName(String brandName);

    Product rating (Long productId);
}
