package etu.nic.store.repository;

import etu.nic.store.model.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserIdOrSessionId(Long userId, String sessionId);
    Optional<Cart> findBySessionId(String sessionId);
}
