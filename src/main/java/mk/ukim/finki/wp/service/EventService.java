package mk.ukim.finki.wp.service;
import mk.ukim.finki.wp.model.Event;
import mk.ukim.finki.wp.model.User;

import java.time.LocalDateTime;
import java.util.*;

public interface EventService {
    List<Event> listAll();
    Optional<Event> findById(Long id);
    List<Event> findByMonth(String month);
    Event create(LocalDateTime startDate, LocalDateTime endDate, String eventName, String eventDescription);
    Event update(Long id, LocalDateTime startDate, LocalDateTime endDate, String eventName, String eventDescription);
    Event delete(Long id);
    List<Event> listEventsByDateBetween(LocalDateTime date1, LocalDateTime date2);
}
