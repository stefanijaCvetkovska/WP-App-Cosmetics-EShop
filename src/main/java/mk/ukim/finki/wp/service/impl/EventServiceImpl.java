package mk.ukim.finki.wp.service.impl;

import mk.ukim.finki.wp.model.Event;
import mk.ukim.finki.wp.model.User;
import mk.ukim.finki.wp.repository.EventRepository;
import mk.ukim.finki.wp.repository.UserRepository;
import mk.ukim.finki.wp.service.EventService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;


    public EventServiceImpl(EventRepository eventRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Event> listAll() {
        return this.eventRepository.findAll();
    }

    @Override
    public Optional<Event> findById(Long id) {
        return this.eventRepository.findById(id);
    }

    @Override
    public List<Event> findByMonth(String month) {
        return this.eventRepository.findAllByMonthEquals(month);
    }

    @Override
    public Event create(LocalDateTime startDate, LocalDateTime endDate, String eventName, String eventDescription) {
        Event event=new Event(startDate,endDate,eventName,eventDescription);
        return this.eventRepository.save(event);
    }

    @Override
    public Event update(Long id, LocalDateTime startDate, LocalDateTime endDate, String eventName, String eventDescription) {
        Event event=this.eventRepository.findById(id).get();
        event.setStartDate(startDate);
        event.setEndDate(endDate);
        event.setEventName(eventName);
        event.setEventDescription(eventDescription);
        event.setMonth(startDate.getMonth().name());
        return this.eventRepository.save(event);
    }

    @Override
    public Event delete(Long id) {
        Event event=this.eventRepository.findById(id).get();
        this.eventRepository.delete(event);
        return event;
    }

    @Override
    public List<Event> listEventsByDateBetween(LocalDateTime date1, LocalDateTime date2) {
        return this.eventRepository.findAllByStartDateBetween(date1, date2);
    }

    @Override
    public void deleteFinished(List<Event> events) {
        LocalDateTime currentTime = LocalDateTime.now();

        for(int i=0;i<events.toArray().length;i++){
            if(events.get(i).getEndDate().isAfter(currentTime))
                events.remove(events.get(i));
        }
        this.eventRepository.saveAll(events);
    }
}
