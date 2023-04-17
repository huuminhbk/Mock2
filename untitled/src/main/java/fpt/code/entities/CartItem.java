package fpt.code.entities;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
@Data
public class CartItem {

    private Product product;

    private Integer quantity;

    public double getTotal() {
        return product.getPrice() * product.getQuantity();
    }

    // getters và setters
}
