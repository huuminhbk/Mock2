package fpt.code.cartservice;

import fpt.code.entities.Cart;
import fpt.code.entities.Product;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
@Service
public class CartServiceImpl implements CartService{
    private final HttpSession session;

    public CartServiceImpl(HttpSession session) {
        this.session = session;
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

    @Override
    public void addProduct(Product product) {
        getCart().addProduct(product);
    }
    @Override
    public void removeProduct(Product product) {
        getCart().removeProduct(product);
    }


}
