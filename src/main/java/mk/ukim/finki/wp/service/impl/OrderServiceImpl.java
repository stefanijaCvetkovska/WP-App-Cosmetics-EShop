package mk.ukim.finki.wp.service.impl;

import mk.ukim.finki.wp.model.Order;
import mk.ukim.finki.wp.model.Product;
import mk.ukim.finki.wp.model.ShoppingCart;
import mk.ukim.finki.wp.model.User;
import mk.ukim.finki.wp.model.enumerations.PaymentMethods;
import mk.ukim.finki.wp.model.enumerations.ShoppingCartStatus;
import mk.ukim.finki.wp.repository.OrderRepository;
import mk.ukim.finki.wp.repository.ProductRepository;
import mk.ukim.finki.wp.repository.ShoppingCartRepository;
import mk.ukim.finki.wp.service.OrderService;
import mk.ukim.finki.wp.service.ShoppingCartService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final ShoppingCartService shoppingCartService;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public OrderServiceImpl(ShoppingCartService shoppingCartService, ShoppingCartRepository shoppingCartRepository, ProductRepository productRepository, OrderRepository orderRepository) {
        this.shoppingCartService = shoppingCartService;
        this.shoppingCartRepository = shoppingCartRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public Order purchase(Long cartId, String address, String phone, PaymentMethods paymentMethods) {
        List<Product> products = this.shoppingCartService.listAllProductsInShoppingCart(cartId);

        User user = this.shoppingCartService.findById(cartId).getUser();
        ShoppingCart shoppingCart = this.shoppingCartService.getActiveShoppingCart(user.getId());
        Double subtotal = this.shoppingCartService.totalPrice(user.getId());

        for (int i = 0; i < products.size(); i++) {
            Product p = this.productRepository.findById(products.get(i).getId()).get();
            int q = p.getQuantity() - 1;
            p.setQuantity(q);
            this.productRepository.save(p);
        }

        Double shipping = this.shippingCalculation(cartId);
        Double totalPrice = shipping + subtotal;

        shoppingCart.getProducts().clear();
        shoppingCart.setStatus(ShoppingCartStatus.FINISHED);
        this.shoppingCartRepository.save(shoppingCart);

        return this.orderRepository.save(new Order(shoppingCart, address, phone, shipping, totalPrice, paymentMethods));
    }

    @Override
    public Double shippingCalculation(Long cartId) {
        User user = this.shoppingCartService.findById(cartId).getUser();
        Double total = this.shoppingCartService.totalPrice(user.getId());

        if (total < 15.0) {
            return 3.99;
        } else if (total >= 15.0 && total < 50.0) {
            return 2.99;
        } else if (total >= 50.0 && total < 100.0) {
            return 1.99;
        } else {
            return 0.0;
        }
    }
}
