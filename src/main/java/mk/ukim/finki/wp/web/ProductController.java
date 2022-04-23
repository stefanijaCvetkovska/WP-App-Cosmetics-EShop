package mk.ukim.finki.wp.web;

import mk.ukim.finki.wp.model.*;
import mk.ukim.finki.wp.repository.UserRepository;
import mk.ukim.finki.wp.service.*;
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
    private final ReviewService reviewService;

    public ProductController(ProductService productService,
                             CategoryService categoryService,
                             BrandService brandService,
                             ShoppingCartService shoppingCartService,
                             UserRepository userRepository,
                             ReviewService reviewService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.brandService = brandService;
        this.shoppingCartService = shoppingCartService;
        this.userRepository = userRepository;
        this.reviewService = reviewService;
    }

    @GetMapping
    public String getProducts(HttpServletRequest request, Model model) {
        return findPaginatedProducts(request, 1, "id", "asc", null, null, null, null, "", model);
    }

    @GetMapping("/page/{pageNo}")
    public String findPaginatedProducts(HttpServletRequest request,
                                        @PathVariable(value = "pageNo") int pageNum,
                                        @RequestParam(defaultValue = "id") String sortField,
                                        @RequestParam(defaultValue = "asc") String sortDir,
                                        @RequestParam(required = false) Long categoryId,
                                        @RequestParam(required = false) Long brandId,
                                        @RequestParam(required = false) Double price1,
                                        @RequestParam(required = false) Double price2,
                                        @RequestParam(required = false) String name,
                                        Model model) {

        String email = request.getRemoteUser();
        User user = this.userRepository.findByEmail(email);
        ShoppingCart shoppingCart = this.shoppingCartService.getActiveShoppingCart(user.getId());
        model.addAttribute("productsInCart", shoppingCart.getProducts().size());

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
                         @RequestParam String description,
                         @RequestParam Long brand,
                         @RequestParam Long category,
                         @RequestParam String image) {
        this.productService.create(name, price, quantity, description, brand, category, image);
        return "redirect:/products";
    }

    @PostMapping("/add/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam String name,
                         @RequestParam Double price,
                         @RequestParam Integer quantity,
                         @RequestParam String description,
                         @RequestParam Long brand,
                         @RequestParam Long category,
                         @RequestParam String image) {
        this.productService.update(id, name, price, quantity, description, brand, category, image);
        return "redirect:/products";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        this.productService.delete(id);
        return "redirect:/products";
    }

    @GetMapping("/details/{id}")
    public String getProductDetails(@PathVariable Long id, HttpServletRequest request, Model model) {
        model.addAttribute("numReviews",this.reviewService.listAllByProduct(id).size());
        return findPaginatedProductDetails(request, id, 1, model);
    }

    @GetMapping("/details/{id}/page/{pageNo}")
    public String findPaginatedProductDetails(HttpServletRequest request,
                                              @PathVariable Long id,
                                              @PathVariable(value = "pageNo") int pageNo,
                                              Model model) {
        String email = request.getRemoteUser();
        User user = this.userRepository.findByEmail(email);
        ShoppingCart shoppingCart = this.shoppingCartService.getActiveShoppingCart(user.getId());
        model.addAttribute("productsInCart", shoppingCart.getProducts().size());

        Product product = this.productService.findById(id).get();

        int pageSize = 6;
        Page<Review> page = this.reviewService.listAllByProduct(pageNo, pageSize, id);
        List<Review> reviews = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        List<Review> reviewsByUser = this.reviewService.listAllByProductAndUser(id, email);

        model.addAttribute("reviewsByUser", reviewsByUser);

        model.addAttribute("product", product);
        model.addAttribute("reviews", reviews);
        model.addAttribute("bodyContent", "product-details");
        return "master-template";
    }
}
