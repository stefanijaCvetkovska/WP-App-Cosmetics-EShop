package mk.ukim.finki.wp.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String month;

    private String eventName;

    private String eventDescription;

    public Event(LocalDateTime startDate, LocalDateTime endDate, String eventName, String eventDescription) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.month = startDate.getMonth().name();
        this.eventName = eventName;
        this.eventDescription = eventDescription;
    }
}
