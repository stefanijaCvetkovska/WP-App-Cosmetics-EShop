package mk.ukim.finki.wp.repository;

import mk.ukim.finki.wp.model.ShoppingCart;
import mk.ukim.finki.wp.model.User;
import mk.ukim.finki.wp.model.enumerations.ShoppingCartStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    Optional<ShoppingCart> findByUserAndStatus(User user, ShoppingCartStatus status);
}
