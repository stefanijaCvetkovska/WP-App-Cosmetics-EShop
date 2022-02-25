package mk.ukim.finki.wp.service.impl;

import mk.ukim.finki.wp.model.Product;
import mk.ukim.finki.wp.model.ShoppingCart;
import mk.ukim.finki.wp.model.User;
import mk.ukim.finki.wp.model.enumerations.ShoppingCartStatus;
import mk.ukim.finki.wp.model.exceptions.ProductAlreadyInShoppingCartException;
import mk.ukim.finki.wp.model.exceptions.ProductNotFoundException;
import mk.ukim.finki.wp.model.exceptions.ShoppingCartIdNotFoundException;
import mk.ukim.finki.wp.model.exceptions.UserNotFoundException;
import mk.ukim.finki.wp.repository.ShoppingCartRepository;
import mk.ukim.finki.wp.repository.UserRepository;
import mk.ukim.finki.wp.service.ProductService;
import mk.ukim.finki.wp.service.ShoppingCartService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
    private final ProductService productService;

    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository,
                                   UserRepository userRepository,
                                   ProductService productService) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.userRepository = userRepository;
        this.productService = productService;
    }


    @Override
    public List<Product> listAllProductsInShoppingCart(Long cartId) {
        if (!this.shoppingCartRepository.findById(cartId).isPresent())
            throw new ShoppingCartIdNotFoundException();
        return this.shoppingCartRepository.findById(cartId).get().getProducts();
    }

    @Override
    public ShoppingCart getActiveShoppingCart(Long userId) {
        User user = this.userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        return this.shoppingCartRepository
                .findByUserAndStatus(user, ShoppingCartStatus.CREATED)
                .orElseGet(() -> {
                    ShoppingCart cart = new ShoppingCart(user);
                    return this.shoppingCartRepository.save(cart);
                });
    }

    @Override
    public ShoppingCart addProductToShoppingCart(Long userId, Long productId) {
        ShoppingCart shoppingCart = this.getActiveShoppingCart(userId);
        Product product = this.productService.findById(productId)
                .orElseThrow(ProductNotFoundException::new);

        if (shoppingCart.getProducts()
                .stream()
                .filter(i -> i.getId().equals(productId))
                .collect(Collectors.toList()).size() > 0)
            throw new ProductAlreadyInShoppingCartException();
        shoppingCart.getProducts().add(product);

        return this.shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart removeProductFromShoppingCart(Long userId, Long productId) {
        ShoppingCart shoppingCart = this.getActiveShoppingCart(userId);
        Product product = this.productService.findById(productId)
                .orElseThrow(ProductNotFoundException::new);

        shoppingCart.getProducts().remove(product);

        return this.shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public double totalPrice(Long userId) {
        ShoppingCart shoppingCart = this.getActiveShoppingCart(userId);
        List<Product> products = shoppingCart.getProducts();
        double total = 0.0;
        for (int i = 0; i < products.size(); i++) {
            total += products.get(i).getPrice();
        }
        total = Math.round(total * 100.0) / 100.0;
        return total;
    }
}
