package fpt.code.entities;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("Session")
public class Cart {
    private List<Product> listProducts = new ArrayList<>();

    public List<Product> getListProducts() {
        return listProducts;
    }

    public void setListProducts(List<Product> listProducts) {
        this.listProducts = listProducts;
    }

    public void addProduct(Product product) {
        listProducts.add(product);
    }

    public void removeProduct(Product product) {
        listProducts.remove(product);
    }

    public void clear() {
        listProducts.clear();
    }

    public int getTotalQuantity() {
        int totalQuantity = 0;
        for (Product item : listProducts) {
            totalQuantity += item.getQuantity();
        }
        return totalQuantity;
    }

    public double getTotalPrice() {
        double totalPrice = 0;
        for (Product item : listProducts) {
            totalPrice += item.getPrice() * item.getQuantity();
        }
        return totalPrice;
    }
}
