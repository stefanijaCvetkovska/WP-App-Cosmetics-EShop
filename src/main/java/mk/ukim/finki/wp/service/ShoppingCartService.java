package mk.ukim.finki.wp.service;

import mk.ukim.finki.wp.model.Product;
import mk.ukim.finki.wp.model.ShoppingCart;

import java.util.HashMap;
import java.util.List;

public interface ShoppingCartService {
    List<Product> listAllProductsInShoppingCart(Long cartId);

    ShoppingCart getActiveShoppingCart(Long userId);

    ShoppingCart addProductToShoppingCart(Long userId, Long productId);

    ShoppingCart updateShoppingCart(Long userId, Long productId, int newQuantity);

    ShoppingCart removeProductFromShoppingCart(Long userId, Long productId);

    double totalPrice(Long userId);

    int sizeOfListProductsById(Long cartId, Long productId);

    List productOrderQuantity(Long cartId);

    ShoppingCart findById(Long cartId);
}

