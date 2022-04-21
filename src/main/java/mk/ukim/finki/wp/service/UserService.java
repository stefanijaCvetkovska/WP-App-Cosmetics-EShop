package mk.ukim.finki.wp.service;

import mk.ukim.finki.wp.model.Event;
import mk.ukim.finki.wp.model.User;
import mk.ukim.finki.wp.web.dto.UserRegistrationDto;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.*;

public interface UserService extends UserDetailsService{
	User save(UserRegistrationDto registrationDto);

	User addEventToInterested(Long userId, Long eventId);

	User removeEventFromInterested(Long userId, Long eventId);

	List<Event> listAllEventsInInterested(Long userId);

	int allUsers ();
}
