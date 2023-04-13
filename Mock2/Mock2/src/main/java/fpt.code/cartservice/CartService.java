package fpt.code.cartservice;

import fpt.code.entities.Cart;
import fpt.code.entities.Product;

public interface CartService {
    Cart getCart();
    void clearCart();
    void addProduct(Product product);
    void removeProduct(Product product);



}
