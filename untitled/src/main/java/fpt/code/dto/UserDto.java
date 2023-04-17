package fpt.code.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements Serializable {
	private final static long serialVersionUID = 1L;

	private Integer id;

	private String username;

	private String email;

	private boolean enable;

}
