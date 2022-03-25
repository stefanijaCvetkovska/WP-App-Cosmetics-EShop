package mk.ukim.finki.wp.service.impl;

import mk.ukim.finki.wp.model.Product;
import mk.ukim.finki.wp.model.ShoppingCart;
import mk.ukim.finki.wp.model.User;
import mk.ukim.finki.wp.model.enumerations.ShoppingCartStatus;
import mk.ukim.finki.wp.model.exceptions.ProductAlreadyInShoppingCartException;
import mk.ukim.finki.wp.model.exceptions.ProductNotFoundException;
import mk.ukim.finki.wp.model.exceptions.ShoppingCartIdNotFoundException;
import mk.ukim.finki.wp.model.exceptions.UserNotFoundException;
import mk.ukim.finki.wp.repository.ProductRepository;
import mk.ukim.finki.wp.repository.ShoppingCartRepository;
import mk.ukim.finki.wp.repository.UserRepository;
import mk.ukim.finki.wp.service.ProductService;
import mk.ukim.finki.wp.service.ShoppingCartService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
    private final ProductService productService;
    private final ProductRepository productRepository;

    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository,
                                   UserRepository userRepository,
                                   ProductService productService, ProductRepository productRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.userRepository = userRepository;
        this.productService = productService;
        this.productRepository = productRepository;
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
    public ShoppingCart updateShoppingCart(Long userId, Long productId, int newQuantity) {
        ShoppingCart shoppingCart = this.getActiveShoppingCart(userId);
        Product product = this.productService.findById(productId)
                .orElseThrow(ProductNotFoundException::new);
        int totalQuantity = product.getQuantity();
        int currentQuantity = this.sizeOfListProductsById(shoppingCart.getId(), productId);

        if (newQuantity > 0 && newQuantity <= totalQuantity) {
            for (int i = 1; i < currentQuantity; i++) {
                shoppingCart.getProducts().remove(product);
            }
            for (int i = 1; i < newQuantity; i++) {
                shoppingCart.getProducts().add(product);
            }
        }
        return this.shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart removeProductFromShoppingCart(Long userId, Long productId) {
        ShoppingCart shoppingCart = this.getActiveShoppingCart(userId);

        List<Product> products = this.productRepository.findAllById(Collections.singleton(productId));
        shoppingCart.getProducts().removeAll(products);

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

    @Override
    public int sizeOfListProductsById(Long cartId, Long productId) {

        List<Product> products = this.listAllProductsInShoppingCart(cartId);
        int num = 0;

        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equals(productId)) {
                num += 1;
            }
        }
        return num;
    }

    @Override
    public List<List<Object>> productOrderQuantity(Long cartId) {
        List<Product> products = this.listAllProductsInShoppingCart(cartId);
        List<Long> productIds = new LinkedList<>();
        List result = new LinkedList();

        for (int i = 0; i < products.size(); i++) {
            Long productId = products.get(i).getId();
            if (!productIds.contains(productId)) {
                productIds.add(productId);
            }
        }

        for (int i = 0; i < productIds.size(); i++) {
            List sublist = new LinkedList<>();
            Product product = this.productService.findById(productIds.get(i)).get();
            sublist.add(product);
            sublist.add(sizeOfListProductsById(cartId, productIds.get(i)));
            result.add(sublist);
        }

        return result;
    }

    @Override
    public ShoppingCart findById(Long cartId) {
        return this.shoppingCartRepository.findById(cartId).get();
    }
}
