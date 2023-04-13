package fpt.code.controllers;

import fpt.code.cartservice.CartService;
import fpt.code.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/shopping")
public class CartController {
    @Autowired
    private CartService cartService;
    @PostMapping("/add")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> addProductToCart(@RequestBody Product product){
        cartService.addProduct(product);
        return new ResponseEntity<>("đã thêm sản phẩm vào giỏ hàng thành công",HttpStatus.OK);
    }
    @DeleteMapping("/clear")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> clearThisCart(){
        cartService.clearCart();
        return new ResponseEntity<>("Giỏ hàng trống",HttpStatus.OK);
    }
    @GetMapping("/view")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> viewCart(){
        return new ResponseEntity<>(cartService.getCart(),HttpStatus.OK);
    }
    @PutMapping("/remove")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> removeProductFromCart(@RequestBody Product product){
        cartService.removeProduct(product);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
