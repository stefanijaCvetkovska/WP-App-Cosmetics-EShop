package mk.ukim.finki.wp.web;

import mk.ukim.finki.wp.model.Event;
import mk.ukim.finki.wp.model.User;
import mk.ukim.finki.wp.model.enumerations.Months;
import mk.ukim.finki.wp.repository.UserRepository;
import mk.ukim.finki.wp.service.EventService;
import mk.ukim.finki.wp.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;
    private final UserRepository userRepository;
    private final UserService userService;

    public EventController(EventService eventService, UserRepository userRepository, UserService userService) {
        this.eventService = eventService;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping
    public String getEventPage(@RequestParam(required = false) String filter,
                               @RequestParam(required = false) String interest,
                               HttpServletRequest req,
                               Model model) {

        List<Event> events;
        User user = this.userRepository.findByEmail(req.getRemoteUser());
        List<Event> interested = this.userService.listAllEventsInInterested(user.getId());


        if (filter != null ) {
            events = this.eventService.findByMonth(filter);
        } else if(interest != null){
            events = this.userService.listAllEventsInInterested(user.getId());
        }else{
            events = this.eventService.listAll();
        }

        List months = Arrays.asList(Months.values());

        model.addAttribute("months", months);
        model.addAttribute("events", events);
        model.addAttribute("interested", interested);
        model.addAttribute("bodyContent", "events");
        return "master-template";
    }


    @GetMapping("/add-form")
    public String getEventsAddPage(Model model) {
        model.addAttribute("bodyContent", "events-form");
        return "master-template";
    }

    @PostMapping("/add")
    public String create(@RequestParam String eventName,
                         @RequestParam String eventDescription,
                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        this.eventService.create(startDate, endDate, eventName, eventDescription);
        return "redirect:/events";
    }

    @GetMapping("/edit-form/{id}")
    public String getEventsEditPage(@PathVariable Long id, Model model) {
        Event event = this.eventService.findById(id).get();
        model.addAttribute("event", event);
        model.addAttribute("bodyContent", "events-form");
        return "master-template";
    }

    @PostMapping("/add/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam String eventName,
                         @RequestParam String eventDescription,
                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        this.eventService.update(id, startDate, endDate, eventName, eventDescription);
        return "redirect:/events";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        this.eventService.delete(id);
        return "redirect:/events";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/add-event/{id}")
    public String addEventToInterested(@PathVariable Long id, HttpServletRequest req) {
        try {
            String email = req.getRemoteUser();
            User user = this.userRepository.findByEmail(email);
            this.userService.addEventToInterested(user.getId(), id);
            return "redirect:/events?interest=INTERESTED";
        } catch (RuntimeException exception) {
            return "redirect:/events?error=" + exception.getMessage();
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/remove-event/{id}")
    public String removeEventFromInterested(@PathVariable Long id, HttpServletRequest req) {
        try {
            String email = req.getRemoteUser();
            User user = this.userRepository.findByEmail(email);
            this.userService.removeEventFromInterested(user.getId(), id);
            return "redirect:/events";
        } catch (RuntimeException exception) {
            return "redirect:/events?error=" + exception.getMessage();
        }
    }
}
