package fpt.code.payload.request;

import javax.persistence.Column;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

	@DecimalMin(value = "0.0", inclusive = false, message = "price must be positive")
	@Digits(integer = 20, fraction = 2, message = "price format mismatch up to 3 integers and 2 decimals!")
	private Double price;

	@NotBlank(message = "Product's name is mandatory")
	@Column(nullable = false, length = 50,unique = true)
	@Size(min = 3, max = 50, message = "The number of characters in the field Product's name cannot be less than 3 and greater than 50")
	private String product_name;

	@NotBlank(message = "description is mandatory")
	@Column(nullable = false, length = 1000)
	@Size(min = 3, max = 1000, message = "The number of characters in the field description cannot be less than 10 and greater than 1000")
	private String description;

	@NotNull(message = "quantity is mandatory")
	@Column(columnDefinition = "int default(0)", length = 11)
	@Min(value = 0, message = "value quantity cannot less than 0")
	private Integer quantity;

}
