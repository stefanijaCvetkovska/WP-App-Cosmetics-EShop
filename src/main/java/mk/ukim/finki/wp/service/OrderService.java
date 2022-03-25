package mk.ukim.finki.wp.service;

import mk.ukim.finki.wp.model.Order;
import mk.ukim.finki.wp.model.enumerations.PaymentMethods;

public interface OrderService {
    Order purchase(Long cartId, String address, String phone, PaymentMethods paymentMethods);
    Double shippingCalculation(Long cartId);
}
