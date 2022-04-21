package mk.ukim.finki.wp.service.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import mk.ukim.finki.wp.model.Event;
import mk.ukim.finki.wp.model.Role;
import mk.ukim.finki.wp.model.User;
import mk.ukim.finki.wp.model.exceptions.EventNotFoundException;
import mk.ukim.finki.wp.model.exceptions.ShoppingCartIdNotFoundException;
import mk.ukim.finki.wp.model.exceptions.UserNotFoundException;
import mk.ukim.finki.wp.repository.EventRepository;
import mk.ukim.finki.wp.service.UserService;
import mk.ukim.finki.wp.web.dto.UserRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import mk.ukim.finki.wp.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private EventRepository eventRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, EventRepository eventRepository) {
        super();
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public User save(UserRegistrationDto registrationDto) {
        User user = new User(registrationDto.getFirstName(),
                registrationDto.getLastName(), registrationDto.getEmail(),
                passwordEncoder.encode(registrationDto.getPassword()), Arrays.asList(new Role("ROLE_USER")));
        return userRepository.save(user);
    }

    @Override
    public User addEventToInterested(Long userId, Long eventId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        Event event = this.eventRepository.findById(eventId)
                .orElseThrow(EventNotFoundException::new);

        user.getEvents().add(event);
        return this.userRepository.save(user);
    }

    @Override
    public User removeEventFromInterested(Long userId, Long eventId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        Event event = this.eventRepository.findById(eventId)
                .orElseThrow(EventNotFoundException::new);
        user.getEvents().remove(event);
        return this.userRepository.save(user);
    }

    @Override
    public List<Event> listAllEventsInInterested(Long userId) {
        return this.userRepository.findById(userId).get().getEvents();
    }

    @Override
    public int allUsers() {
        return this.userRepository.findAll().size();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

}
