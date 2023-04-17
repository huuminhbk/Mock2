package fpt.code.shopservice;

import fpt.code.entities.Cart;
import fpt.code.entities.Order;
import fpt.code.entities.User;

import java.util.List;

public interface OrderService {
    void createOrder(User user, Cart cart);
    List<Order> getOrdersByUserId(Long userId);
}
