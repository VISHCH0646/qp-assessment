package com.example.demo.service;

import java.util.List;
import java.util.Map;

import com.example.demo.dto.GenericGroceryItemDto;

public interface GroceryItemService {
	
	public Map<String,Object> createGroceryItem(GenericGroceryItemDto createGroceryItemRequest);

	public List<GenericGroceryItemDto> getGroceryItems();

	public GenericGroceryItemDto getGroceryItemById(Long id);

	public void deleteById(Long id);

	public GenericGroceryItemDto updateGroceryItemById(Long id, GenericGroceryItemDto groceryItem);

}
