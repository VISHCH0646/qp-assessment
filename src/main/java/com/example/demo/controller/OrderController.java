package com.example.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.GenericGroceryItemDto;
import com.example.demo.entity.GroceryItem;
import com.example.demo.exception.GroceryManagementException;
import com.example.demo.repository.GroceryItemRepo;
import com.example.demo.service.GroceryItemService;
import com.example.demo.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("v1/order")
@Slf4j
@RequiredArgsConstructor
public class OrderController {
	
	private final OrderService orderService;
	
	@PostMapping
	public ResponseEntity<Map<String,Object>> placeOrder(@RequestBody List<GenericGroceryItemDto> items) {
		log.info("Request received for order place with details {}",items);
		return ResponseEntity.ok(orderService.placeOrder(items));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Object> getOrderDetails(@PathVariable(value = "id", required = true) Long id) {
		log.info("Request received to fetch order with ID: {}",id);
		return ResponseEntity.ok(orderService.getOrderDetails(id));
	}
	
}
