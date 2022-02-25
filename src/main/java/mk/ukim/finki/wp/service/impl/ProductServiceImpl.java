package mk.ukim.finki.wp.service.impl;

import mk.ukim.finki.wp.model.Brand;
import mk.ukim.finki.wp.model.Category;
import mk.ukim.finki.wp.model.Product;
import mk.ukim.finki.wp.model.exceptions.InvalidBrandIdException;
import mk.ukim.finki.wp.model.exceptions.InvalidCategoryIdException;
import mk.ukim.finki.wp.repository.BrandRepository;
import mk.ukim.finki.wp.repository.CategoryRepository;
import mk.ukim.finki.wp.repository.ProductRepository;
import mk.ukim.finki.wp.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository, BrandRepository brandRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Product> listAll() {
        return this.productRepository.findAll();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return this.productRepository.findById(id);
    }

    @Override
    public Product create(String name, Double price, Integer quantity, Long brandId, Long categoryId, String image) {
        Brand brand=this.brandRepository.findById(brandId).orElseThrow(InvalidBrandIdException::new);
        Category category=this.categoryRepository.findById(categoryId).orElseThrow(InvalidCategoryIdException::new);
        Product product=new Product(name,price,quantity,brand,category,image);
        return this.productRepository.save(product);
    }

    @Override
    public Product update(Long id, String name, Double price, Integer quantity, Long brandId, Long categoryId, String image) {
        Brand brand=this.brandRepository.findById(brandId).orElseThrow(InvalidBrandIdException::new);
        Category category=this.categoryRepository.findById(categoryId).orElseThrow(InvalidCategoryIdException::new);
        Product product=this.productRepository.findById(id).get();
        product.setName(name);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setBrand(brand);
        product.setCategory(category);
        product.setImage(image);
        return this.productRepository.save(product);
    }

    @Override
    public Product delete(Long id) {
        Product product=this.productRepository.findById(id).get();
        this.productRepository.delete(product);
        return product;
    }

    @Override
    public Page<Product> listAllProducts(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.productRepository.findAll(pageable);
    }


    @Override
    public Page<Product> listProductsByCategory(int pageNo, int pageSize, String sortField, String sortDirection, Long categoryId) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

        Category category = categoryId != null ? this.categoryRepository.findById(categoryId).orElse((Category) null) : null;
        if (category != null) {
            return this.productRepository.findAllByCategory(category, pageable);
        } else {
            return this.productRepository.findAll(pageable);
        }
    }

    @Override
    public Page<Product> listProductsByBrand(int pageNo, int pageSize, String sortField, String sortDirection, Long brandId) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

        Brand brand = brandId != null ? this.brandRepository.findById(brandId).orElse((Brand) null) : null;
        if (brand != null) {
            return this.productRepository.findAllByBrand(brand, pageable);
        } else {
            return this.productRepository.findAll(pageable);
        }
    }

    @Override
    public Page<Product> listProductsByPriceBetween(int pageNo, int pageSize, String sortField, String sortDirection, Double price1, Double price2) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

        return this.productRepository.findAllByPriceBetween(price1, price2, pageable);
    }

    @Override
    public Page<Product> listProductsByNameLike(int pageNo, int pageSize, String sortField, String sortDirection, String name) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

        String nameLike = "%" + name + "%";
        if (name != null) {
            return this.productRepository.findAllByNameLikeIgnoreCase(nameLike, pageable);
        } else {
            return this.productRepository.findAll(pageable);
        }
    }

}
