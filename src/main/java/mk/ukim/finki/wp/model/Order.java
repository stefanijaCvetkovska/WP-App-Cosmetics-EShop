package mk.ukim.finki.wp.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.wp.model.enumerations.PaymentMethods;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime purchaseDate;

    private String address;

    private String phone;

    private Double shipping;

    private Double totalPrice;

    @ManyToOne
    private ShoppingCart shoppingCart;

    @ManyToMany
    private List<Product> products;

    @Enumerated(EnumType.STRING)
    private PaymentMethods paymentMethods;

    public Order(ShoppingCart shoppingCart, String address, String phone, Double shipping, Double totalPrice, PaymentMethods paymentMethods) {
        this.purchaseDate = LocalDateTime.now();
        this.shoppingCart = shoppingCart;
        this.address = address;
        this.phone = phone;
        this.shipping = shipping;
        this.totalPrice = totalPrice;
        this.paymentMethods = paymentMethods;
    }
}
