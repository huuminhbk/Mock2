package fpt.code.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import fpt.code.dto.UserDto;
import fpt.code.entities.ERole;
import fpt.code.entities.User;
import fpt.code.mail.MailService;
import fpt.code.entities.Role;
import fpt.code.payload.request.UserRequest;
import fpt.code.payload.response.MessageResponse;
import fpt.code.service.RoleService;
import fpt.code.service.UserService;
import fpt.code.utils.FileUploadUtil;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {
	private static final Logger logger = Logger.getLogger(UserController.class);

	@Autowired
	UserService userService;

	@Autowired
	RoleService roleService;

	@Autowired
	MailService mailService;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	ModelMapper modelMapper;

	@GetMapping(value = "/all")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> displayUsers(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "3") int size) {

		Map<String, Object> map = new HashMap<String, Object>();
		List<User> listUser = new ArrayList<User>();
		List<UserDto> listUserDto = new ArrayList<UserDto>();
		try {
			Pageable pageable = PageRequest.of(page, size);
			Page<User> pageTuts;
			pageTuts = userService.findUsersByRole(pageable);
			listUser = pageTuts.getContent();

			if (listUser.isEmpty()) {
				map.put("status", 0);
				map.put("message", "Data users not exist !!!");


				logger.warn("Data users not exist !!!");
				return new ResponseEntity<>(map, HttpStatus.NO_CONTENT);
			}

			listUserDto = listUser.stream().map(e -> modelMapper.map(e, UserDto.class)).collect(Collectors.toList());

			map.put("status", 1);
			map.put("message", "display users successfully !!!");
			map.put("users", listUserDto);
			map.put("currentPage", pageTuts.getNumber() + 1);
			map.put("totalItems", pageTuts.getTotalElements());
			map.put("totalPages", pageTuts.getTotalPages());
			map.put("pageSize", size);
			logger.info("display users successfully !!!");

			return new ResponseEntity<>(map, HttpStatus.OK);

		} catch (Exception ex) {
			ex.getStackTrace();
			map.clear();
			map.put("status", 0);
			map.put("message", "display users failed !!!!");
			logger.error("display users failed !!!!");

			return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@DeleteMapping(value = "/delete/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		try {
			Optional<User> user = userService.findById(id);
			if (user.isPresent()) {
				userService.deleteById(id);
				map.put("status", 1);
				map.put("message", "delete user successfully !!!");
				logger.info("delete successfully");

				return new ResponseEntity<>(map, HttpStatus.OK);
			} else {
				map.put("status", 0);
				map.put("message", "not found user with id = " + id);
				logger.warn("not found user with id = " + id);

				return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
			}

		} catch (Exception ex) {
			ex.getStackTrace();
			map.clear();
			map.put("status", 0);
			map.put("message", "delete user failed !!!");
			logger.error("delete user failed !!!");
			return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(value = "/create", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> createUser(
			@Valid @RequestPart(name = "userRequest", required = true) UserRequest userRequest,
			@RequestParam(name = "avatar", required = false) MultipartFile avatar

	) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			if (userService.existsByUsername(userRequest.getUsername())) {
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
			}

			if (userService.existsByEmail(userRequest.getEmail())) {
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
			}

			Set<Role> roles = new HashSet<>();
			String fileName = StringUtils.cleanPath(avatar.getOriginalFilename());
			String fileCode = FileUploadUtil.saveFile(fileName, avatar);
			User user = new User();
			user.setEmail(userRequest.getEmail());
			user.setUsername(userRequest.getUsername());
//			user.setEnable(true);
			Role userRole = roleService.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
			user.setRoles(roles);
			user.setPassword(encoder.encode(userRequest.getPassword()));
			user.setAvatar("/downloadFile/" + fileCode);
			System.out.println("sgasgjka:::" + user);
			mailService.register(user);

			userService.save(user);
			UserDto userDto = modelMapper.map(user, UserDto.class);
			map.put("status", 1);
			map.put("data", userDto);
			map.put("message", "add user successfully.  Please check your email to activate your account !!!!");
			logger.info("add user successfully.  Please check your email to activate your account !!!!");
			return new ResponseEntity<>(map, HttpStatus.CREATED);

		} catch (Exception ex) {
			ex.getStackTrace();
			map.clear();
			map.put("status", 0);
			map.put("message", "create user failed !!!!");
			logger.error("create user failed !!!");

			return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/verify")
	public ResponseEntity<?> verifyUser(@RequestParam String code) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (mailService.verify(code) == true) {
				map.put("status", 1);
				map.put("message", "kích hoạt tài khoản thành công");
				logger.info("kích hoạt tài khoản thành công");
			} else {
				map.put("status", 0);
				map.put("message", "kích hoạt tài khoản thất bại");
				logger.info("kích hoạt tài khoản thất bại");
			}
			return new ResponseEntity<>(map, HttpStatus.OK);
		} catch (Exception ex) {
			ex.getStackTrace();
			map.clear();
			map.put("status", 0);
			map.put("message", "send mail  failed !!!!");
			logger.error("send mail  failed !!!!");
			return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
