package mk.ukim.finki.wp.repository;

import mk.ukim.finki.wp.model.Product;
import mk.ukim.finki.wp.model.Review;
import mk.ukim.finki.wp.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findAllByProduct(Product product, Pageable pageable);
    List<Review> findAllByProductAndUser(Product product, User user);
}
