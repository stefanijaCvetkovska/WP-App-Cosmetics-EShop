package mk.ukim.finki.wp.web;

import mk.ukim.finki.wp.model.*;
import mk.ukim.finki.wp.repository.UserRepository;
import mk.ukim.finki.wp.service.BrandService;
import mk.ukim.finki.wp.service.CategoryService;
import mk.ukim.finki.wp.service.ProductService;
import mk.ukim.finki.wp.service.ShoppingCartService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final BrandService brandService;
    private final ShoppingCartService shoppingCartService;
    private final UserRepository userRepository;

    public ProductController(ProductService productService,
                             CategoryService categoryService,
                             BrandService brandService,
                             ShoppingCartService shoppingCartService,
                             UserRepository userRepository) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.brandService = brandService;
        this.shoppingCartService = shoppingCartService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String getProducts(Model model) {
        return findPaginatedProducts(1, "id", "asc", null, null, null, null, "", model);
    }

    @GetMapping("/page/{pageNo}")
    public String findPaginatedProducts(@PathVariable(value = "pageNo") int pageNum,
                                        @RequestParam(defaultValue = "id") String sortField,
                                        @RequestParam(defaultValue = "asc") String sortDir,
                                        @RequestParam(required = false) Long categoryId,
                                        @RequestParam(required = false) Long brandId,
                                        @RequestParam(required = false) Double price1,
                                        @RequestParam(required = false) Double price2,
                                        @RequestParam(required = false) String name,
                                        Model model) {
        int pageSize = 6;
        Page<Product> page;

        if (categoryId != null) {
            page = this.productService.listProductsByCategory(pageNum, pageSize, sortField, sortDir, categoryId);
        } else if (brandId != null) {
            page = this.productService.listProductsByBrand(pageNum, pageSize, sortField, sortDir, brandId);
        } else if (price1 != null && price2 != null) {
            page = this.productService.listProductsByPriceBetween(pageNum, pageSize, sortField, sortDir, price1, price2);
        } else if (name != null) {
            page = this.productService.listProductsByNameLike(pageNum, pageSize, sortField, sortDir, name);
        } else {
            page = this.productService.listAllProducts(pageNum, pageSize, sortField, sortDir);
        }

        List<Product> products = page.getContent();

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("brandId", brandId);
        model.addAttribute("price1", price1);
        model.addAttribute("price2", price2);
        model.addAttribute("name", name);

        model.addAttribute("products", products);
        model.addAttribute("categories", this.categoryService.listAll());
        model.addAttribute("brands", this.brandService.listAll());
        model.addAttribute("price1", price1);
        model.addAttribute("price2", price2);

        model.addAttribute("bodyContent", "products");
        return "master-template";
    }

    @GetMapping("/add-form")
    public String getAddProducts(Model model) {
        List<Category> categories = this.categoryService.listAll();
        List<Brand> brands = this.brandService.listAll();
        model.addAttribute("categories", categories);
        model.addAttribute("brands", brands);
        model.addAttribute("bodyContent", "products-form");
        return "master-template";
    }

    @GetMapping("/edit-form/{id}")
    public String getEditProducts(@PathVariable Long id, Model model) {
        Product product = this.productService.findById(id).get();
        List<Category> categories = this.categoryService.listAll();
        List<Brand> brands = this.brandService.listAll();
        model.addAttribute("product", product);
        model.addAttribute("categories", categories);
        model.addAttribute("brands", brands);
        model.addAttribute("bodyContent", "products-form");
        return "master-template";
    }

    @PostMapping("/add")
    public String create(@RequestParam String name,
                         @RequestParam Double price,
                         @RequestParam Integer quantity,
                         @RequestParam Long brand,
                         @RequestParam Long category,
                         @RequestParam String image) {
        this.productService.create(name, price, quantity, brand, category, image);
        return "redirect:/products";
    }

    @PostMapping("/add/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam String name,
                         @RequestParam Double price,
                         @RequestParam Integer quantity,
                         @RequestParam Long brand,
                         @RequestParam Long category,
                         @RequestParam String image) {
        this.productService.update(id, name, price, quantity, brand, category, image);
        return "redirect:/products";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        this.productService.delete(id);
        return "redirect:/products";
    }
}
