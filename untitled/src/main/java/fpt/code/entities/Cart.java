package fpt.code.entities;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Scope("Session")
@Data
public class Cart {

    private Map<Integer, CartItem> cartItemsMap = new HashMap<>();

    public void addProduct(Product product) {
        if (cartItemsMap.containsKey(product.getId())) {
            CartItem cartItem = cartItemsMap.get(product.getId());
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            cartItemsMap.put(product.getId(), cartItem);
        }
    }

    public void removeProduct(Product product) {
        if (cartItemsMap.containsKey(product.getId())) {
            CartItem cartItem = cartItemsMap.get(product.getId());
            if (cartItem.getQuantity() > 1) {
                cartItem.setQuantity(cartItem.getQuantity() - 1);
            } else {
                cartItemsMap.remove(product.getId());
            }
        }
    }
    public void clear() {
        cartItemsMap.clear();
    }

    public Double getTotal() {
        double total = 0;
        for (CartItem cartItem : cartItemsMap.values()) {
            total += cartItem.getTotal();
        }
        return total;
    }

    // getters và setters
}
