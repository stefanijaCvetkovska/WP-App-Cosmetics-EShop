package mk.ukim.finki.wp.web;

import mk.ukim.finki.wp.model.Event;
import mk.ukim.finki.wp.model.Order;
import mk.ukim.finki.wp.model.User;
import mk.ukim.finki.wp.repository.UserRepository;
import mk.ukim.finki.wp.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/user/profile")
public class UserProfileController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final OrderService orderService;
    private final EventService eventService;
    private final ReviewService reviewService;

    public UserProfileController(UserService userService, UserRepository userRepository, OrderService orderService, EventService eventService, ReviewService reviewService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.orderService = orderService;
        this.eventService = eventService;
        this.reviewService = reviewService;
    }

    @GetMapping
    public String getUserProfilePage(Model model, HttpServletRequest request) {
        String email = request.getRemoteUser();
        User user = this.userRepository.findByEmail(email);
        List<Event> events = this.userService.listAllEventsInInterested(user.getId());
        List<Order> orders = this.orderService.listAllOrdersByUser(user.getId());
        model.addAttribute("events", events);
        model.addAttribute("orders", orders);
        model.addAttribute("user", user);
        model.addAttribute("bodyContent", "user-profile");
        model.addAttribute("reviewsNum",this.reviewService.reviewsByUser(user));
        return "master-template";
    }
}
