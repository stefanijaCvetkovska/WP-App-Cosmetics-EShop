package mk.ukim.finki.wp.service;

import mk.ukim.finki.wp.model.User;
import mk.ukim.finki.wp.web.dto.UserRegistrationDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService{
	User save(UserRegistrationDto registrationDto);
}
