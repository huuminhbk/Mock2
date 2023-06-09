package fpt.code.service;

import fpt.code.entities.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface ProductService {
	public Page<Product> findAll(Pageable pageable);

	public void save(Product product);

	public Optional<Product> findById(Integer id);

	public void deleteById(Integer id);

	public Optional<Product> findByProduct_name(String product_name);

}
