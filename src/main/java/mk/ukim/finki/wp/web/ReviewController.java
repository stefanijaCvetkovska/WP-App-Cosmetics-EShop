package mk.ukim.finki.wp.web;

import mk.ukim.finki.wp.model.Product;
import mk.ukim.finki.wp.model.Review;
import mk.ukim.finki.wp.model.User;
import mk.ukim.finki.wp.repository.UserRepository;
import mk.ukim.finki.wp.service.ProductService;
import mk.ukim.finki.wp.service.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final ProductService productService;
    private final UserRepository userRepository;

    public ReviewController(ReviewService reviewService, ProductService productService, UserRepository userRepository) {
        this.reviewService = reviewService;
        this.productService = productService;
        this.userRepository = userRepository;
    }

    @GetMapping("/{id}/reviews-form")
    public String getReviewsForm(@PathVariable Long id, Model model) {
        Product product = this.productService.findById(id).get();
        model.addAttribute("product", product);
        model.addAttribute("bodyContent", "review-form");
        return "master-template";
    }

    @PostMapping("/{id}/add-review")
    public String addReview(@PathVariable Long id,
                            HttpServletRequest request,
                            @RequestParam(required = false) String comment,
                            @RequestParam float stars,
                            Model model) {
        this.reviewService.create(request.getRemoteUser(), id, comment, stars);
        this.productService.rating(id);
        model.addAttribute("id", this.productService.findById(id));
        return "redirect:/products/details/{id}";
    }

    @GetMapping("/{id}/reviews-form/{reviewId}")
    public String getReviewsFormEdit(@PathVariable Long id,
                                     @PathVariable Long reviewId,
                                     Model model) {
        Product product = this.productService.findById(id).get();
        Review review = this.reviewService.findById(reviewId).get();
        model.addAttribute("product", product);
        model.addAttribute("review", review);
        model.addAttribute("bodyContent", "review-form");
        return "master-template";
    }

    @PostMapping("/{id}/add-review/{reviewId}")
    public String editReview(@PathVariable Long id,
                             @PathVariable Long reviewId,
                             HttpServletRequest request,
                             @RequestParam(required = false) String comment,
                             @RequestParam float stars,
                             Model model) {
        this.reviewService.edit(reviewId, comment, stars);
        this.productService.rating(id);
        model.addAttribute("id", this.productService.findById(id));
        model.addAttribute("reviewId", this.reviewService.findById(id));
        return "redirect:/products/details/{id}#comments";
    }

    @PostMapping("/{id}/delete-review/{reviewId}")
    public String deleteReview(@PathVariable Long id,
                               @PathVariable Long reviewId,
                               Model model){
        this.reviewService.delete(reviewId);
        model.addAttribute("id", this.productService.findById(id));
        model.addAttribute("reviewId", this.reviewService.findById(id));
        return "redirect:/products/details/{id}#comments";
    }

}
