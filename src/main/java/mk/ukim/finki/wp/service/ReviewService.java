package mk.ukim.finki.wp.service;

import mk.ukim.finki.wp.model.Product;
import mk.ukim.finki.wp.model.Review;
import mk.ukim.finki.wp.model.User;
import org.springframework.data.domain.Page;

import java.util.*;

public interface ReviewService {
    Review create(String email, Long productId, String comment, float stars);
    Review edit(Long id, String comment, float stars);
    Review delete(Long id);
    Optional<Review> findById(Long id);
    Page<Review> listAllByProduct(int pageNo, int pageSize, Long productId);
    List<Review> listAllByProductAndUser(Long productId, String email);
    List<Review> listAllByProduct(Long productId);
}
