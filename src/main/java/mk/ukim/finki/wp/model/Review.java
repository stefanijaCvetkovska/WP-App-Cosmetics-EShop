package mk.ukim.finki.wp.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "reviews")
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Product product;
    private String comment;
    private float stars;

    public Review(User user, Product product, String comment, float stars) {
        this.user = user;
        this.product = product;
        this.comment = comment;
        this.stars = stars;
    }
}
