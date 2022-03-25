package mk.ukim.finki.wp.web;

import mk.ukim.finki.wp.model.Product;
import mk.ukim.finki.wp.model.ShoppingCart;
import mk.ukim.finki.wp.model.User;
import mk.ukim.finki.wp.repository.UserRepository;
import mk.ukim.finki.wp.service.ShoppingCartService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.expression.Lists;
import org.thymeleaf.expression.Sets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/shopping-cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    private final UserRepository userRepository;

    public ShoppingCartController(ShoppingCartService shoppingCartService,
                                  UserRepository userRepository) {
        this.shoppingCartService = shoppingCartService;
        this.userRepository = userRepository;
    }

    @GetMapping()
    public String getShoppingCartPage(HttpServletRequest req, Model model) {
        String email = req.getRemoteUser();
        User user = this.userRepository.findByEmail(email);
        ShoppingCart shoppingCart = this.shoppingCartService.getActiveShoppingCart(user.getId());

        model.addAttribute("total", shoppingCartService.totalPrice(user.getId()));

        List<Product> productList = this.shoppingCartService.listAllProductsInShoppingCart(shoppingCart.getId());

        model.addAttribute("productQuantity", this.shoppingCartService.productOrderQuantity(shoppingCart.getId()));

        Set<Product> set = new HashSet<>(productList);
        productList.clear();
        productList.addAll(set);

        model.addAttribute("products", productList);
        model.addAttribute("productsInCart", shoppingCart.getProducts().size());
        model.addAttribute("bodyContent", "shopping-cart");
        return "master-template";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/add-product/{id}")
    public String addProductToCart(@PathVariable Long id, HttpServletRequest req, Model model) {
        try {
            String email = req.getRemoteUser();
            User user = this.userRepository.findByEmail(email);
            ShoppingCart shoppingCart = this.shoppingCartService.getActiveShoppingCart(user.getId());
            this.shoppingCartService.addProductToShoppingCart(user.getId(), id);
            return "redirect:/shopping-cart";
        } catch (RuntimeException exception) {
            return "redirect:/shopping-cart?error=" + exception.getMessage();
        }
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/edit-cart/{productId}")
    public String updateShoppingCart(@PathVariable Long productId,
                                     @RequestParam int newQuantity,
                                     HttpServletRequest req,
                                     Model model) {
        try {
            String email = req.getRemoteUser();
            User user = this.userRepository.findByEmail(email);
            ShoppingCart shoppingCart = this.shoppingCartService.getActiveShoppingCart(user.getId());
            this.shoppingCartService.updateShoppingCart(user.getId(), productId, newQuantity);
            return "redirect:/shopping-cart";
        } catch (RuntimeException exception) {
            return "redirect:/shopping-cart?error=" + exception.getMessage();
        }
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/remove-product/{id}")
    public String removeProductFromCart(@PathVariable Long id, HttpServletRequest req) {
        try {
            String email = req.getRemoteUser();
            User user = this.userRepository.findByEmail(email);
            this.shoppingCartService.removeProductFromShoppingCart(user.getId(), id);
            return "redirect:/shopping-cart";
        } catch (RuntimeException exception) {
            return "redirect:/shopping-cart?error=" + exception.getMessage();
        }
    }
}
