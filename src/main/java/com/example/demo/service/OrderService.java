package com.example.demo.service;

import java.util.List;
import java.util.Map;

import com.example.demo.dto.GenericGroceryItemDto;

public interface OrderService {
	
	public Map<String,Object> placeOrder(List<GenericGroceryItemDto> items);

	public Object getOrderDetails(Long id);
	
}
