package fpt.code.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@Entity
@Table(name = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@DecimalMin(value = "0.0", inclusive = false, message = "price must be positive")
	@Digits(integer = 20, fraction = 2, message = "price format mismatch up to 3 integers and 2 decimals!")
	private Double price;

	@NotBlank(message = "Product's name is mandatory")
	@Column(nullable = false, length = 50, unique = true)
	@Size(min = 3, max = 50, message = "The number of characters in the field Product's name cannot be less than 3 and greater than 50")
	private String product_name;

	@NotBlank(message = "product_image is mandatory")
	private String product_image;

	@NotBlank(message = "description is mandatory")
	@Column(nullable = false, length = 1000)
	@Size(min = 3, max = 1000, message = "The number of characters in the field description cannot be less than 10 and greater than 1000")
	private String description;

	@NotNull(message = "quantity is mandatory")
	@Column(columnDefinition = "int default(0)", length = 11)
	@Min(value = 0, message = "value quantity cannot less than 0")
	private Integer quantity;
	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	@OneToMany(mappedBy = "product",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	private List<OrderDetail> orderDetailList;

}
