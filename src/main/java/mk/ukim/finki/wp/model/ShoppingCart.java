package mk.ukim.finki.wp.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.wp.model.enumerations.ShoppingCartStatus;

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
