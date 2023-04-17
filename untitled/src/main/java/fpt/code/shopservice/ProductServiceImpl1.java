package fpt.code.shopservice;

import fpt.code.entities.Product;
import fpt.code.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl1 implements ProductService1 {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Integer productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        return productOptional.orElse(null);
    }

    @Override
    public List<Product> getProductsByCategoryId(Long id) {
        return null;
    }


    @Override
    public void saveProduct(Product product) {
        productRepository.save(product);
    }
    @Override

    public void deleteProduct(Product product) {
        productRepository.delete(product);
    }
}
