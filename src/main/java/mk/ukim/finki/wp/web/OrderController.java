package mk.ukim.finki.wp.web;

import mk.ukim.finki.wp.model.ShoppingCart;
import mk.ukim.finki.wp.model.User;
import mk.ukim.finki.wp.model.enumerations.PaymentMethods;
import mk.ukim.finki.wp.repository.UserRepository;
import mk.ukim.finki.wp.service.OrderService;
import mk.ukim.finki.wp.service.ShoppingCartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    private final UserRepository userRepository;
    private final ShoppingCartService shoppingCartService;

    public OrderController(OrderService orderService, UserRepository userRepository, ShoppingCartService shoppingCartService) {
        this.orderService = orderService;
        this.userRepository = userRepository;
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping()
    public String getOrdersForm(HttpServletRequest request, Model model) {
        User user = this.userRepository.findByEmail(request.getRemoteUser());
        List paymentMethods = Arrays.asList(PaymentMethods.values());
        ShoppingCart cart = this.shoppingCartService.getActiveShoppingCart(user.getId());
        model.addAttribute("cart", cart);
        model.addAttribute("first", user.getFirstName());
        model.addAttribute("last", user.getLastName());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("paymentMethods", paymentMethods);
        model.addAttribute("bodyContent", "order-form");
        return "master-template";
    }

    @PostMapping("/purchase/{cart}")
    public String purchaseOrder(@PathVariable Long cart,
                                @RequestParam String address,
                                @RequestParam String phone,
                                @RequestParam PaymentMethods payment) {
        this.orderService.purchase(cart, address, phone, payment);
        return "redirect:/order/finished-order";
    }

    @GetMapping("/finished-order")
    public String finishedOrder(Model model){
        model.addAttribute("bodyContent", "finished-order");
        return "master-template";
    }
}
