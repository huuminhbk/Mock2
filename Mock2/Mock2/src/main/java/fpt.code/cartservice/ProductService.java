package fpt.code.cartservice;

import fpt.code.advice.ProductNotFoundException;
import fpt.code.entities.Product;
import fpt.code.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    public Product loadProductByName(String name){
        return productRepository.findProductByName(name)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with name: " + name));
    }
}
