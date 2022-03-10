package mk.ukim.finki.wp.web;

import mk.ukim.finki.wp.model.Event;
import mk.ukim.finki.wp.model.enumerations.Months;
import mk.ukim.finki.wp.repository.UserRepository;
import mk.ukim.finki.wp.service.EventService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;
    private final UserRepository userRepository;

    public EventController(EventService eventService, UserRepository userRepository) {
        this.eventService = eventService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String getEventPage(@RequestParam(required = false) String filterMonth, Model model) {

        List<Event> events;
        if (filterMonth != null) {
            events = this.eventService.findByMonth(filterMonth);
        }
        else{
            events = this.eventService.listAll();
        }

        List months = Arrays.asList(Months.values());
        model.addAttribute("months", months);
        model.addAttribute("events", events);
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
}
