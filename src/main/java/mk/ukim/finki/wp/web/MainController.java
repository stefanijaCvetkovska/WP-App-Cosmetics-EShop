package mk.ukim.finki.wp.web;

import mk.ukim.finki.wp.model.ShoppingCart;
import mk.ukim.finki.wp.model.User;
import mk.ukim.finki.wp.repository.UserRepository;
import mk.ukim.finki.wp.service.BrandService;
import mk.ukim.finki.wp.service.ShoppingCartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class MainController {

    private final UserRepository userRepository;
    private final ShoppingCartService shoppingCartService;
    private final BrandService brandService;

    public MainController(UserRepository userRepository, ShoppingCartService shoppingCartService, BrandService brandService) {
        this.userRepository = userRepository;
        this.shoppingCartService = shoppingCartService;
        this.brandService = brandService;
    }

    @GetMapping("/")
    public String home(HttpSession session, HttpServletRequest request, Model model) {

        try {
            String email = request.getRemoteUser();
            User user = this.userRepository.findByEmail(email);
            session.setAttribute("user", user.getFirstName());
        } catch (RuntimeException e) {
        }

        model.addAttribute("brands", this.brandService.listAll());
        model.addAttribute("bodyContent", "index");
        return "master-template";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }
}
