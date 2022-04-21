package mk.ukim.finki.wp.service;

import mk.ukim.finki.wp.model.Order;
import mk.ukim.finki.wp.model.enumerations.PaymentMethods;

import java.util.List;

public interface OrderService {
    Order purchase(Long cartId, String address, String phone, PaymentMethods paymentMethods);

    Double shippingCalculation(Long cartId);

    Double sales(int month);

    Double expenses(int month);

    Double profit(int month);

    List<Order> listAllOrdersByUser(Long userId);

    int allOrders ();
}
