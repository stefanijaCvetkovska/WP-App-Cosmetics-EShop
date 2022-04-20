package mk.ukim.finki.wp.web;

import mk.ukim.finki.wp.model.Brand;
import mk.ukim.finki.wp.model.Event;
import mk.ukim.finki.wp.model.User;
import mk.ukim.finki.wp.repository.UserRepository;
import mk.ukim.finki.wp.service.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/admin/profile")
public class AdminProfileController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final ProductService productService;
    private final BrandService brandService;
    private final OrderService orderService;
    private final EventService eventService;

    public AdminProfileController(UserService userService,
                                  UserRepository userRepository, ProductService productService, BrandService brandService, OrderService orderService, EventService eventService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.productService = productService;
        this.brandService = brandService;
        this.orderService = orderService;
        this.eventService = eventService;
    }

    @GetMapping
    public String getUserProfilePage(Model model, HttpServletRequest request) {
        String email = request.getRemoteUser();
        User user = this.userRepository.findByEmail(email);
//        List<Event> events = this.userService.listAllEventsInInterested(user.getId());
        List<Event> events = this.eventService.listAll();
        model.addAttribute("events", events);
        model.addAttribute("categoriesChart", getCategoriesChart());
        model.addAttribute("brandsChart", getBrandChart());
        model.addAttribute("eventsChart", getEventChartData(events));
        model.addAttribute("profitChart", getProfitChartData());
        model.addAttribute("bodyContent", "admin-profile");
        return "master-template";
    }

    private List<List<Object>> getCategoriesChart() {
        return List.of(
                List.of("Makeup", this.productService.listAllProductsByCategoryName("Makeup").size()),
                List.of("Hair care", this.productService.listAllProductsByCategoryName("Hair care").size()),
                List.of("Skin care", this.productService.listAllProductsByCategoryName("Skin care").size()),
                List.of("Body care", this.productService.listAllProductsByCategoryName("Body care").size()),
                List.of("Nails", this.productService.listAllProductsByCategoryName("Nails").size()),
                List.of("Perfume", this.productService.listAllProductsByCategoryName("Perfume").size())
        );
    }

    private List<List<Object>> getBrandChart() {
        List<Brand> brands = this.brandService.listAll();
        List result = new LinkedList();
        for (int i = 0; i < brands.size(); i++) {
            String brandName = brands.get(i).getName();
            List<Object> brand = List.of(brandName, this.productService.listAllProductsByBrandName(brandName).size());
            result.add(brand);
        }
        return result;
    }

    private List<List<Object>> getEventChartData(List<Event> events) {
        List result = new LinkedList();

        for (int i = 0; i < events.size(); i++) {
            String eventName = events.get(i).getEventName();
            int startMonth = events.get(i).getStartDate().getMonthValue();
            int endMonth = events.get(i).getEndDate().getMonthValue();
            List<Object> event = List.of(eventName, startMonth, endMonth);
            result.add(event);
        }

        return result;
    }

    private List<List<Object>> getProfitChartData() {
        List result = new LinkedList();
        for (int i = 1; i <= 12; i++) {
            List<Object> perMonth = List.of(i, this.orderService.sales(i),
                    this.orderService.expenses(i), this.orderService.profit(i));
            result.add(perMonth);
        }
        return result;
    }
}