package fpt.code.shopservice;


import fpt.code.entities.Product;

import java.util.List;


public interface ProductService1 {
    List<Product> getAllProducts();
    Product getProductById(Integer productId);
    List<Product> getProductsByCategoryId(Long id);
    void saveProduct(Product product);
    void deleteProduct(Product product);

}
