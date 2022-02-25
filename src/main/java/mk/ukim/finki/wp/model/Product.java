package mk.ukim.finki.wp.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "products")
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double price;
    private Integer quantity;
    private Double rating;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Brand brand;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Category category;
    @Column(length = 2500)
    private String image;

    public Product(String name, Double price, Integer quantity, Brand brand, Category category, String image) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.rating = 0.0;
        this.brand = brand;
        this.category = category;
        this.image = image;
    }
}
