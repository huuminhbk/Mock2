package fpt.code.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "products")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  product_id;

    private String product_name;
    private String description ;
    private double price ;
    private int quantity ;
    @ManyToMany(mappedBy = "productList",fetch = FetchType.LAZY)
    private Set<Order> listOrders;

}
