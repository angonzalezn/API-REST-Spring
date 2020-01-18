package com.ssotom.controller;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssotom.model.Customer;
import com.ssotom.model.Region;
import com.ssotom.response.ErrorResponse;
import com.ssotom.response.ResponseMessage;
import com.ssotom.service.ICustomerService;
import com.ssotom.service.IFileService;

@CrossOrigin()
@RestController
@RequestMapping("/api/customers")
public class CustomerRestControler {
	
	@Autowired
	private ICustomerService customerService;
	
	@Autowired
	private IFileService fileService;
	
	private final static String PICTURE_PATH = "uploads" + File.separator + "customers" + File.separator + "pictures";
	
	@GetMapping()
	public List<Customer> index() {
		return customerService.findAll();
	}
	
	@GetMapping("/p/{page}")
	public Page<Customer> index(@PathVariable Integer page) {
		return customerService.findAll(PageRequest.of(page, 10));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		Optional<Customer> customer = customerService.findById(id);
		return customer.isPresent()
				?new ResponseEntity<Customer>(customer.get(), HttpStatus.OK)
				:new ResponseEntity<ErrorResponse>(new ErrorResponse(HttpStatus.NOT_FOUND, "Customer not found", 
						"Customer not found with id = " + id), HttpStatus.NOT_FOUND);
	}
	
	@PostMapping()
	public ResponseEntity<?> create(@Valid @RequestBody Customer customer, BindingResult result) {
		if(!customerService.isValidEmail(customer)) {
			FieldError error = new FieldError("customer", "email", customer.getEmail() + " in use");
			result.addError(error);
		}
		if(result.hasErrors()) {
			return returnError(result);
		}
		try {
			Customer newCustomer = customerService.save(customer);
			return new ResponseEntity<Customer>(newCustomer, HttpStatus.CREATED);
		} catch (DataAccessException e) {
			return returnInternalServerError(e.getMostSpecificCause().getLocalizedMessage());
		}	
	}
	
	@PutMapping()
	public ResponseEntity<?> update(@Valid @RequestBody Customer customer, BindingResult result) {
		return create(customer, result);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
		try {
			Optional<Customer> customer = customerService.findById(id);
			customer.ifPresent(this::deletePicture);
			customerService.delete(id);
			return new ResponseEntity<ResponseMessage>(new ResponseMessage("Customer removed with success"), HttpStatus.OK);
		} catch (DataAccessException e) {
			return returnInternalServerError(e.getMostSpecificCause().getLocalizedMessage());

		}	
	}
	
	@PostMapping("/upload")
	public ResponseEntity<?> uploadPicture(@RequestParam("file") MultipartFile file, @RequestParam("id") Long id) {
		if(!file.isEmpty()) {
			Optional<Customer> customer = customerService.findById(id);
			if(customer.isPresent()) {
				Customer cus = customer.get();
				String fileName = "";
				
				try {
					fileName = fileService.upload(file, PICTURE_PATH, ""+id);
				} catch (IOException e) {
					return returnInternalServerError(e.getMessage());
				}
				
				deletePicture(cus);
				cus.setPicture(fileName);
				customerService.save(cus);
				return new ResponseEntity<Customer>(cus, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<ErrorResponse>(new ErrorResponse(HttpStatus.NOT_FOUND, "Customer not found", 
						"Customer not found with id = " + id), HttpStatus.NOT_FOUND);
			}			
		} else {
			return new ResponseEntity<ErrorResponse>(new ErrorResponse(HttpStatus.BAD_REQUEST, "Missing parameter", 
					"The file parameter is necessary"), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/upload/picture/{fileName:.+}")
	public ResponseEntity<?> showPicture(@PathVariable String fileName) {
		Path filePath = fileService.getPath(PICTURE_PATH, fileName);	
		try {
			Resource resource = fileService.get(filePath);
			if(!resource.exists() || !resource.isReadable()) {
				filePath = fileService.getPath("src/main/resources/static/img", "not_user.png");
				resource = fileService.get(filePath);
			}
			HttpHeaders header = new HttpHeaders();
			header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");
			return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
		} catch (MalformedURLException e) {
			return returnInternalServerError(e.getMessage());
		}
	}
		
	private void deletePicture(Customer customer) {
		String oldFileName = customer.getPicture();
		if(oldFileName !=null && oldFileName.length() > 0) {
			Path oldFilePath = fileService.getPath(PICTURE_PATH, oldFileName);
			fileService.delete(oldFilePath);
		}
	}
	
	@GetMapping("/regions")
	public List<Region> getRegions() {
		return customerService.findAllRegions();
	}
	
	private ResponseEntity<?> returnError(BindingResult result) { 
		List<String> errors = new LinkedList<String>();
		for (FieldError error : result.getFieldErrors()) {
	        errors.add(error.getField() + ": " + error.getDefaultMessage());
	    }
		return new ResponseEntity<ErrorResponse>(new ErrorResponse(HttpStatus.BAD_REQUEST, "The request has some errors", 
				errors), HttpStatus.BAD_REQUEST);
	}
	
	private ResponseEntity<?> returnInternalServerError(String message) {
		return new ResponseEntity<ErrorResponse>(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Fatal error", 
				message), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
