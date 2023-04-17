package fpt.code.controllers;

import fpt.code.entities.Cart;
import fpt.code.entities.Product;
import fpt.code.entities.User;
import fpt.code.repository.UserRepository;
import fpt.code.shopservice.CartService;

import fpt.code.shopservice.OrderService;
import fpt.code.shopservice.ProductService1;
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


    @Autowired
    private ProductService1 productService1;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> addProductToCart(@RequestParam Integer id) {
        Product product = productService1.getProductById(id);
        cartService.addToCart(product);
        return new ResponseEntity<>("đã thêm sản phẩm vào giỏ hàng thanhf công",HttpStatus.OK);
    }

    @PostMapping("/remove")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> removeFromCart(@RequestParam Integer id) {
        Product product = productService1.getProductById(id);
        if (product == null) {
            return ResponseEntity.badRequest().body("Product not found");
        }
        cartService.removeFromCart(product);
        return new ResponseEntity<>("đã xóa sản phẩm khỏi giỏ hàng thành công",HttpStatus.OK);
    }
    @GetMapping("/view")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> viewCart() {
        Cart cart = cartService.getCart();
        return new ResponseEntity<>(cart,HttpStatus.OK);
    }
    @PostMapping("/checkout")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> checkoutCart(Authentication authentication) {
        String username = authentication.getName();
        Optional<User> user = userRepository.findByUsername(username);
        Cart cart = cartService.getCart();
        orderService.createOrder(user.get(),cart);
        cartService.clearCart();
        return new ResponseEntity<>("Hóa đơn đã được lưu thành công", HttpStatus.OK);
    }



}
