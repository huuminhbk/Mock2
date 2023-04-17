package fpt.code.shopservice;

import fpt.code.entities.Cart;
import fpt.code.entities.Product;

public interface CartService {
    void addToCart(Product product) ;
    void removeFromCart(Product product);

    Cart getCart();

    void clearCart() ;
}
