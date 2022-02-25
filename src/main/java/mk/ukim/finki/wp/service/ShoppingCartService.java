package mk.ukim.finki.wp.service;

import mk.ukim.finki.wp.model.Product;
import mk.ukim.finki.wp.model.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {
    List<Product> listAllProductsInShoppingCart(Long cartId);

    ShoppingCart getActiveShoppingCart(Long userId);

    ShoppingCart addProductToShoppingCart(Long userId, Long productId);

    ShoppingCart removeProductFromShoppingCart(Long userId, Long productId);

    double totalPrice(Long userId);
}
