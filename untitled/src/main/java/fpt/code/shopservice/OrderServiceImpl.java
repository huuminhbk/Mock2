package fpt.code.shopservice;


import fpt.code.entities.*;
import fpt.code.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{
    @Autowired
    private OrderRepository orderRepository;

    public void createOrder(User user, Cart cart) {
        Order order = new Order();
        order.setUser(user);
        for (CartItem cartItem : cart.getCartItemsMap().values()) {
            OrderDetail orderDetail = new OrderDetail();

            orderDetail.setProduct(cartItem.getProduct());
            orderDetail.setPrice(cartItem.getProduct().getPrice());
            orderDetail.setQuantity(cartItem.getQuantity());

            order.getOrderDetails().add(orderDetail);
        }
        order.setTotal(cart.getTotal());
        order.setCreatedAt(LocalDateTime.now());
        orderRepository.save(order);
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}
