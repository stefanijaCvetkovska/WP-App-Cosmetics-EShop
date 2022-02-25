package mk.ukim.finki.wp.web;

import mk.ukim.finki.wp.model.ShoppingCart;
import mk.ukim.finki.wp.model.User;
import mk.ukim.finki.wp.repository.UserRepository;
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

	public MainController(UserRepository userRepository, ShoppingCartService shoppingCartService) {
		this.userRepository = userRepository;
		this.shoppingCartService = shoppingCartService;
	}

	@GetMapping("/")
	public String home(HttpServletRequest request, HttpSession session, Model model) {
		String email = request.getRemoteUser();
		User user = this.userRepository.findByEmail(email);
		ShoppingCart shoppingCart = this.shoppingCartService.getActiveShoppingCart(user.getId());

        session.setAttribute("productsInCart", shoppingCart.getProducts().size());
		String userFullName = user.getFirstName() + " " + user.getLastName();
		session.setAttribute("user", userFullName);
		model.addAttribute("bodyContent", "index");
		return "master-template";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

}
