package mk.ukim.finki.wp.repository;

import mk.ukim.finki.wp.model.Product;
import mk.ukim.finki.wp.model.Review;
import mk.ukim.finki.wp.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findAllByProduct(Product product, Pageable pageable);
    List<Review> findAllByProductAndUser(Product product, User user);
    List<Review> findAllByProduct(Product product);
    List<Review> findAllByUser(User user);
}
