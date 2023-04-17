package fpt.code.service.Impl;

import fpt.code.entities.Product;
import fpt.code.repository.ProductRepository;
import fpt.code.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class ProductServiceImpl implements ProductService {
	@Autowired
	ProductRepository productRepository;

	@Override
	public Page<Product> findAll(Pageable pageable) {
		return productRepository.findAll(pageable);
	}

	@Override
	@Transactional
	public void save(Product product) {
		productRepository.save(product);
	}

	@Override
	public Optional<Product> findById(Integer id) {
		return productRepository.findById(id);
	}

	@Override
	@Transactional
	public void deleteById(Integer id) {
		productRepository.deleteById(id);
	}

	@Override
	public Optional<Product> findByProduct_name(String product_name) {
		return productRepository.findByProduct_name(product_name);
	}

}
