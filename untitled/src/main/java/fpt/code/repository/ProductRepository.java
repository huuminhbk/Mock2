package fpt.code.repository;

import fpt.code.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
	Page<Product> findAll(Pageable pageable);

	@Query(value = "SELECT * FROM products p WHERE p.product_name = ?1", nativeQuery = true)
	Optional<Product> findByProduct_name(String product_name);
}
