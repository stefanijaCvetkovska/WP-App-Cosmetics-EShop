package mk.ukim.finki.wp.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.wp.model.enumerations.ShoppingCartStatus;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;

import java.util.*;

@Data
@Entity
@Table(name = "carts")
@NoArgsConstructor
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateCreated;

    @ManyToOne
    private User user;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "carts_products",
            joinColumns = @JoinColumn(
                    name = "shopping_cart_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "products_id", referencedColumnName = "id"))
    private List<Product> products;

    @Enumerated(EnumType.STRING)
    private ShoppingCartStatus status;

    public ShoppingCart(User user){
        this.dateCreated = LocalDateTime.now();
        this.user = user;
        this.products = new ArrayList<>();
        this.status = ShoppingCartStatus.CREATED;
    }
}
