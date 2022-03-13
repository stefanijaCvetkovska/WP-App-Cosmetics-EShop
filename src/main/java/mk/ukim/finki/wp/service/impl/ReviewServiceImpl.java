package mk.ukim.finki.wp.service.impl;

import mk.ukim.finki.wp.model.Category;
import mk.ukim.finki.wp.model.Product;
import mk.ukim.finki.wp.model.Review;
import mk.ukim.finki.wp.model.User;
import mk.ukim.finki.wp.model.exceptions.ProductNotFoundException;
import mk.ukim.finki.wp.model.exceptions.ReviewNotFoundException;
import mk.ukim.finki.wp.repository.ProductRepository;
import mk.ukim.finki.wp.repository.ReviewRepository;
import mk.ukim.finki.wp.repository.UserRepository;
import mk.ukim.finki.wp.service.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository,
                             UserRepository userRepository,
                             ProductRepository productRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Review create(String email, Long productId, String comment, float stars) {
        User user = this.userRepository.findByEmail(email);
        Product product = this.productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);
        Review review = new Review(user, product, comment, stars);
        return this.reviewRepository.save(review);
    }

    @Override
    public Review edit(Long id, String comment, float stars) {
        Review review = this.reviewRepository.findById(id)
                .orElseThrow(ReviewNotFoundException::new);
        review.setComment(comment);
        review.setStars(stars);
        return this.reviewRepository.save(review);
    }

    @Override
    public Review delete(Long id) {
        Review review = this.reviewRepository.findById(id)
                .orElseThrow(ReviewNotFoundException::new);
        this.reviewRepository.delete(review);
        return review;
    }

    @Override
    public Optional<Review> findById(Long id) {
        return this.reviewRepository.findById(id);
    }

    @Override
    public Page<Review> listAllByProduct(int pageNo, int pageSize, Long productId) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Product product = productId != null ? this.productRepository.findById(productId).
                orElse((Product) null) : null;
        return this.reviewRepository.findAllByProduct(product, pageable);
    }

    @Override
    public List<Review> listAllByProductAndUser(Long productId, String email) {
        User user = this.userRepository.findByEmail(email);
        Product product = this.productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);
        return this.reviewRepository.findAllByProductAndUser(product, user);
    }

    @Override
    public List<Review> listAllByProduct(Long productId) {
        Product product = productId != null ? this.productRepository.findById(productId).
                orElse((Product) null) : null;
        return this.reviewRepository.findAllByProduct(product);
    }
}
