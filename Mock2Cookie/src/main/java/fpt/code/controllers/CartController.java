package fpt.code.controllers;

import fpt.code.cartservice.CartService;
import fpt.code.entities.Cart;
import fpt.code.entities.Order;
import fpt.code.entities.Product;
import fpt.code.entities.User;
import fpt.code.repository.OrderRepository;
import fpt.code.repository.UserRepository;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/shopping")
public class CartController {
    public static final Logger logger = Logger.getLogger(CartController.class);

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;


    @PostMapping("/add")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> addProductToCart(@RequestBody Product product){
        logger.info(" thêm sản phẩm vào giỏ hàng");
        cartService.addProduct(product);
        return new ResponseEntity<>("đã thêm sản phẩm vào giỏ hàng thành công",HttpStatus.OK);
    }
    @DeleteMapping("/clear")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> clearThisCart(){
        logger.info(" xóa giỏ hàng");
        cartService.clearCart();
        return new ResponseEntity<>("Giỏ hàng trống",HttpStatus.OK);
    }
    @GetMapping("/view")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> viewCart(){
        return new ResponseEntity<>(cartService.getCart(),HttpStatus.OK);
    }


    @PutMapping("/remove")
    @PreAuthorize("hasRole('USER')  or hasRole('ADMIN')")
    public ResponseEntity<?> removeProductFromCart(@RequestBody Product product){
        logger.info("xóa sản phẩm khỏi giỏ hàng");
        cartService.removeProduct(product);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/checkout")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> checkoutCart(Authentication authentication) {
        String username = authentication.getName();
        Optional<User> user = userRepository.findByUsername(username);
        Cart cart = cartService.getCart();

        // Tạo đối tượng Order mới từ thông tin giỏ hàng
        Order order = new Order();
        order.setPayment_status(true);
        order.setQuantity(cart.getTotalQuantity());
        order.setTotalAmount(cartService.getCart().getTotalPrice());
        order.setUser(user.get());
        orderRepository.save(order);

        cartService.clearCart();
        return new ResponseEntity<>("Hóa đơn đã được lưu thành công", HttpStatus.OK);
    }

}
