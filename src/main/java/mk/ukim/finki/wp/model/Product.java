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

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Brand brand;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Category category;

    @Column(length = 2500)
    private String image;

    @Column(length = 100000)
    private String description;

    private Float rating;

    public Product(String name, Double price, Integer quantity, String description, Brand brand, Category category, String image) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.brand = brand;
        this.category = category;
        this.image = image;
        this.rating = Float.valueOf(0);
    }
}
