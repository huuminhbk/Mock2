package fpt.code.shopservice;

import fpt.code.entities.Cart;
import fpt.code.entities.Product;
import lombok.Data;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Data
@Service
public class CartServiceImpl implements CartService{
    private final HttpSession session;

    public CartServiceImpl(HttpSession session) {
        this.session = session;
    }


    @Override
    public void addToCart(Product product) {
        Cart cart = getCart();
        cart.addProduct(product);
    }

    @Override
    public void removeFromCart(Product product) {
        Cart cart = getCart();
        cart.removeProduct(product);

    }

    @Override
    public Cart getCart() {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    @Override
    public void clearCart() {
        getCart().clear();
    }


}
