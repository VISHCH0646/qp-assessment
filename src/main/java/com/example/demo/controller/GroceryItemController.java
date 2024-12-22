package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.GenericGroceryItemDto;
import com.example.demo.service.GroceryItemService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("v1/grocery-item")
@Slf4j
@RequiredArgsConstructor
public class GroceryItemController {

	private final GroceryItemService groceryItemService;

	@PostMapping
	public ResponseEntity<Map<String, Object>> createGroceryItem(
			@Valid @RequestBody GenericGroceryItemDto createGroceryItemRequest) {
		log.info("Create Item request received with following details {}", createGroceryItemRequest);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(groceryItemService.createGroceryItem(createGroceryItemRequest));
	}

	@GetMapping
	public ResponseEntity<List<GenericGroceryItemDto>> getGroceryItems() {
		log.info("Request received to retrive all grocery items");
		return ResponseEntity.status(HttpStatus.OK).body(groceryItemService.getGroceryItems());
	}

	@GetMapping("/{id}")
	public ResponseEntity<GenericGroceryItemDto> getGroceryItemById(@PathVariable("id") Long id) {
		log.info("Request received to retrive grocery item with ID {}", id);
		return ResponseEntity.status(HttpStatus.OK).body(groceryItemService.getGroceryItemById(id));
	}

	@DeleteMapping("/{id}")
	public void deleteById(@PathVariable(value = "id", required = true) Long id) {
		log.info("Request received to delete grocery item with ID {}", id);
		groceryItemService.deleteById(id);
	}

	@PutMapping("/{id}")
	public ResponseEntity<GenericGroceryItemDto> updateGroceryItemById(
			@PathVariable(value = "id", required = true) Long id, @RequestBody GenericGroceryItemDto groceryItem) {
		log.info("Request received to delete grocery item with ID {}", id);
		return ResponseEntity.ok(groceryItemService.updateGroceryItemById(id, groceryItem));
	}

}
