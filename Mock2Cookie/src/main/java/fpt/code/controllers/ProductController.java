package fpt.code.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import fpt.code.entities.Product;
import fpt.code.payload.request.ProductRequest;
import fpt.code.payload.response.MessageResponse;
import fpt.code.service.ProductService;
import fpt.code.utils.FileDownloadUtil;
import fpt.code.utils.FileUploadUtil;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/product")
public class ProductController {
	private static final Logger logger = Logger.getLogger(ProductController.class);

	@Autowired
	ProductService productService;

	@GetMapping(value = "/all")
	public ResponseEntity<?> displayProduct(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "3") int size) {

		Map<String, Object> map = new HashMap<String, Object>();
		List<Product> listProducts = new ArrayList<Product>();
		try {
			Pageable pageable = PageRequest.of(page, size);
			Page<Product> pageTuts;
			pageTuts = productService.findAll(pageable);
			listProducts = pageTuts.getContent();

			if (listProducts.isEmpty()) {
				map.put("status", 0);
				map.put("message", "Data products not exist !!!");
				logger.error("không có sản phẩm nào ");


				return new ResponseEntity<>(map, HttpStatus.NO_CONTENT);
			}

			map.put("status", 1);
			map.put("message", "display products successfully !!!");
			map.put("products", listProducts);
			map.put("currentPage", pageTuts.getNumber() + 1);
			map.put("totalItems", pageTuts.getTotalElements());
			map.put("totalPages", pageTuts.getTotalPages());
			map.put("pageSize", size);

			return new ResponseEntity<>(map, HttpStatus.OK);

		} catch (Exception ex) {
			ex.getStackTrace();
			map.clear();
			map.put("status", 0);
			map.put("message", "display products failed !!!!");
			logger.error("không thể hiển thị sản phẩm");

			return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping(value = "/create", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> createProduct(
			@Valid @RequestPart(name = "productRequest", required = true) ProductRequest productRequest,
			@RequestParam(name = "file", required = false) MultipartFile file

	) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (productService.findByProduct_name(productRequest.getProduct_name()).isPresent()) {
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Product_Name is already in use!"));
			}
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			String fileCode = FileUploadUtil.saveFile(fileName, file);
			Product product = new Product();
			product.setDescription(productRequest.getDescription());
			product.setPrice(productRequest.getPrice());
			product.setProduct_name(productRequest.getProduct_name());
			product.setQuantity(productRequest.getQuantity());
			product.setProduct_image("/downloadFile/" + fileCode);
			productService.save(product);
			map.put("status", 1);
			map.put("data", product);
			map.put("message", "add product successfully !!!");
			return new ResponseEntity<>(map, HttpStatus.CREATED);

		} catch (Exception ex) {
			ex.getStackTrace();
			map.clear();
			map.put("status", 0);
			map.put("message", "create products failed !!!!");

			return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping(value = "/update/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> updateProductById(@PathVariable Integer id,
			@Valid @RequestPart(name = "productRequest", required = true) ProductRequest productRequest,
			@RequestParam(name = "file", required = false) MultipartFile file) throws IOException {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		try {
			Optional<Product> productOptional = productService.findById(id);
			if (productOptional.isPresent()) {
				Product product = productOptional.get();
				System.out.println("agkjadjfk" + product);
				product.setPrice(productRequest.getPrice());
				product.setDescription(productRequest.getDescription());
				product.setProduct_name(productRequest.getProduct_name());
				product.setQuantity(productRequest.getQuantity());
				String fileName = StringUtils.cleanPath(file.getOriginalFilename());
				String fileCode = FileUploadUtil.saveFile(fileName, file);
				product.setProduct_image("/downloadFile/" + fileCode);
				productService.save(product);
				map.put("status", 1);
				map.put("message", "update product successfully !!! ");
				map.put("data", product);

				return new ResponseEntity<>(map, HttpStatus.OK);
			} else {
				map.put("status", 0);
				map.put("message", "not found product with id = " + id);

				return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {
			e.getStackTrace();
			map.clear();
			map.put("status", 0);
			map.put("message", "update product failed !!!!");

			return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping(value = "/delete/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteProduct(@PathVariable Integer id) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		try {
			Optional<Product> product = productService.findById(id);
			if (product.isPresent()) {
				productService.deleteById(id);
				map.put("status", 1);
				map.put("message", "delete product successfully !!!");

				return new ResponseEntity<>(map, HttpStatus.OK);
			} else {
				map.put("status", 0);
				map.put("message", "not found product with id = " + id);

				return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
			}

		} catch (Exception ex) {
			ex.getStackTrace();
			map.clear();
			map.put("status", 0);
			map.put("message", "delete product failed !!!");

			return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/downloadFile/{fileCode}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> downloadFile(@PathVariable("fileCode") String fileCode) {
		FileDownloadUtil downloadUtil = new FileDownloadUtil();

		Resource resource = null;

		try {
			resource = downloadUtil.getFileAsResource(fileCode);
		} catch (IOException e) {
			return ResponseEntity.internalServerError().build();
		}

		if (resource == null) {
			return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
		}

		String contentType = "application/octet-stream";
		String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, headerValue).body(resource);
	}

}
