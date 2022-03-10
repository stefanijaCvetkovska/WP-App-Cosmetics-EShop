package mk.ukim.finki.wp.repository;

import mk.ukim.finki.wp.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByMonthEquals(String month);
    List<Event> findAllByStartDateBetween(LocalDateTime startDate1, LocalDateTime startDate2);
}
