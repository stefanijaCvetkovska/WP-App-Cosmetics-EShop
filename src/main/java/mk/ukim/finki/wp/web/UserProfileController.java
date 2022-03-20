package mk.ukim.finki.wp.web;

import mk.ukim.finki.wp.model.Brand;
import mk.ukim.finki.wp.model.Event;
import mk.ukim.finki.wp.model.User;
import mk.ukim.finki.wp.repository.UserRepository;
import mk.ukim.finki.wp.service.BrandService;
import mk.ukim.finki.wp.service.ProductService;
import mk.ukim.finki.wp.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/profile")
public class UserProfileController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final ProductService productService;
    private final BrandService brandService;

    public UserProfileController(UserService userService,
                                 UserRepository userRepository, ProductService productService, BrandService brandService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.productService = productService;
        this.brandService = brandService;
    }

    @GetMapping
    public String getUserProfilePage(Model model, HttpServletRequest request) {
        String email = request.getRemoteUser();
        User user = this.userRepository.findByEmail(email);
        List<Event> events = this.userService.listAllEventsInInterested(user.getId());
        model.addAttribute("events", events);
        model.addAttribute("categoriesChart", getCategoriesChart());
        model.addAttribute("brandsChart", getBrandChart());
        model.addAttribute("eventsChart", getEventChartData(request));
        model.addAttribute("bodyContent", "user-profile");
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

    private List<List<Object>> getEventChartData(HttpServletRequest request) {
        String email = request.getRemoteUser();
        User user = this.userRepository.findByEmail(email);
        List<Event> events = this.userService.listAllEventsInInterested(user.getId());
        List result = new LinkedList();

        for (int i = 0; i < events.size(); i++) {
            String eventName = events.get(i).getEventName();

            LocalDateTime startDate = LocalDateTime.parse(events.get(i).getStartDate().toString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            LocalDateTime endDate = LocalDateTime.parse(events.get(i).getEndDate().toString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            List<Object> event = List.of(eventName, startDate, endDate);
            result.add(event);
        }
        return result;
    }

}