package fpt.code.repository;

import fpt.code.entities.Order;
import fpt.code.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    Optional<Product> findOrdersById(Long id);
}
