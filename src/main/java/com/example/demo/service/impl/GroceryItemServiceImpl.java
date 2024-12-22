package com.example.demo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.constants.GroceryManagementConstants;
import com.example.demo.dto.GenericGroceryItemDto;
import com.example.demo.entity.GroceryItem;
import com.example.demo.exception.GroceryManagementException;
import com.example.demo.repository.GroceryItemRepo;
import com.example.demo.service.GroceryItemService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class GroceryItemServiceImpl implements GroceryItemService {

	private final GroceryItemRepo groceryItemRepo;

	@Override
	@Transactional
	public Map<String, Object> createGroceryItem(GenericGroceryItemDto createGroceryItemRequest) {
		log.info("{}", createGroceryItemRequest);

		GroceryItem item = GroceryItem.builder().name(createGroceryItemRequest.getName())
				.price(createGroceryItemRequest.getPrice()).description(createGroceryItemRequest.getDescription())
				.stockQuantity(createGroceryItemRequest.getQuantity()).build();

		var savedItem = groceryItemRepo.save(item);

		return Map.of(GroceryManagementConstants.ID, savedItem.getId(), GroceryManagementConstants.MESSAGE,
				"Item created Successfully");

	}

	@Override
	public List<GenericGroceryItemDto> getGroceryItems() {
		List<GenericGroceryItemDto> response = new ArrayList<>();
		for (GroceryItem item : groceryItemRepo.findAll()) {
			GenericGroceryItemDto itemDto = maptoGroceryItemDto(item);
			response.add(itemDto);
		}
		return response;
	}

	private GenericGroceryItemDto maptoGroceryItemDto(GroceryItem item) {
		GenericGroceryItemDto itemDto = new GenericGroceryItemDto();
		itemDto.setId(item.getId());
		itemDto.setName(item.getName());
		itemDto.setDescription(item.getDescription());
		itemDto.setPrice(item.getPrice());
		itemDto.setQuantity(item.getStockQuantity());
		return itemDto;
	}

	@Override
	public GenericGroceryItemDto getGroceryItemById(Long id) {
		Optional<GroceryItem> item = groceryItemRepo.findById(id);
		if(!item.isPresent()) {
			log.error("No Grocery Item found with ID: {}",id);
			throw new GroceryManagementException(HttpStatus.NOT_FOUND, "No Grocery Item found with ID: "+id);
		}
		GenericGroceryItemDto itemDto = maptoGroceryItemDto(item.get());
		return itemDto;
	}

	@Override
	public void deleteById(Long id) {
		Optional<GroceryItem> item = groceryItemRepo.findById(id);
		if(!item.isPresent()) {
			log.error("No Grocery Item found with ID: {}",id);
			throw new GroceryManagementException(HttpStatus.NOT_FOUND, "No Grocery Item found with ID: "+id);
		}
		groceryItemRepo.delete(item.get());
	}

	@Override
	public GenericGroceryItemDto updateGroceryItemById(Long id, GenericGroceryItemDto groceryItem) {
		Optional<GroceryItem> item = groceryItemRepo.findById(id);
		if(!item.isPresent()) {
			log.error("No Grocery Item found with ID: {}",id);
			throw new GroceryManagementException(HttpStatus.NOT_FOUND, "No Grocery Item found with ID: "+id);
		}
		GroceryItem groceryItemObj = item.get();
		
		Optional.ofNullable(groceryItem.getName()).ifPresent((name)->groceryItemObj.setName(name));
		Optional.ofNullable(groceryItem.getDescription()).ifPresent((desc)->groceryItemObj.setDescription(desc));
		Optional.ofNullable(groceryItem.getPrice()).ifPresent((price)->groceryItemObj.setPrice(price));
		Optional.ofNullable(groceryItem.getQuantity()).ifPresent((quantity)->groceryItemObj.setStockQuantity(quantity));
		
		return maptoGroceryItemDto(groceryItemRepo.save(groceryItemObj));
		
	}

}
