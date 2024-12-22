package com.example.demo.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.constants.GroceryManagementConstants;
import com.example.demo.dto.GenericGroceryItemDto;
import com.example.demo.dto.OrderDetailsDto;
import com.example.demo.entity.GroceryItem;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import com.example.demo.exception.GroceryManagementException;
import com.example.demo.repository.GroceryItemRepo;
import com.example.demo.repository.OrderRepo;
import com.example.demo.service.OrderService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final GroceryItemRepo groceryItemRepo;

	private final OrderRepo orderRepo;

	@Override
	@Transactional
	public Map<String, Object> placeOrder(List<GenericGroceryItemDto> items) {
		log.info("Order Place request received with {}", items);
		List<Long> itemIds = new ArrayList<>();
		items.stream().forEach((item) -> {
			if (ObjectUtils.isEmpty(item.getId()) || ObjectUtils.isEmpty(item.getQuantity())
					|| item.getQuantity() < 1) {
				log.error("Item ID or Quantity should not be blank");
				throw new GroceryManagementException(HttpStatus.BAD_REQUEST, "Item ID or Quantity should not be blank");
			}
			if (itemIds.contains(item.getId())) {
				log.error("Item ID should not be repeated in request");
				throw new GroceryManagementException(HttpStatus.BAD_REQUEST,
						"Item ID should not be repeated in request");
			}
			itemIds.add(item.getId());
		});
		List<GroceryItem> groceryItems = groceryItemRepo.getByIds(itemIds);

		List<OrderItem> orderItems = new ArrayList<>();
		Double totalAmount = 0.0;
		Order o = Order.builder().shippingAddress("Pune").userId(15523L).build();
		for (GenericGroceryItemDto item : items) {
			GroceryItem groceryItem = groceryItems.stream().filter(e -> e.getId().equals(item.getId())).findFirst()
					.get();
			validateStockAndProduct(item, groceryItem);
			groceryItem.setStockQuantity(groceryItem.getStockQuantity() - item.getQuantity());
			OrderItem orderItem = new OrderItem();
			orderItem.setQuantity(item.getQuantity());
			orderItem.setGroceryItem(groceryItem);
			Double itemTotal = groceryItem.getPrice() * item.getQuantity();
			orderItem.setPrice(itemTotal);
			totalAmount += itemTotal;
			orderItem.setOrder(o);

			orderItems.add(orderItem);
		}

		o.setTotalAmount(totalAmount);
		o.setOrderItems(orderItems);
		Order order = orderRepo.save(o);

		return Map.of(GroceryManagementConstants.ID, order.getId(), GroceryManagementConstants.MESSAGE,
				"Order Placed with Order ID " + order.getId(), "Total Amount", totalAmount);
	}

	private void validateStockAndProduct(GenericGroceryItemDto item, GroceryItem groceryItem) {
		if (ObjectUtils.isEmpty(groceryItem)) {
			log.error("Item with ID: {} not present in Inventory", item.getId());
			throw new GroceryManagementException(HttpStatus.BAD_REQUEST,
					String.format("Item with ID: %s not present in Inventory", item.getId()));
		}
		if (groceryItem.getStockQuantity() < item.getQuantity()) {
			log.error("Stock for Id: {} is less than requested quantity.", item.getId());
			throw new GroceryManagementException(HttpStatus.BAD_REQUEST,
					String.format("Stock for %s is less than requested quantity. Current stock: %s",
							groceryItem.getName(), groceryItem.getStockQuantity()));
		}
	}

	@Override
	public Object getOrderDetails(Long id) {
		Order order = orderRepo.findById(id).get();
		if (ObjectUtils.isEmpty(order)) {
			log.error("No Order found with ID: {}", id);
			throw new GroceryManagementException(HttpStatus.NOT_FOUND, "No Order found with ID: " + id);
		}
		List<GenericGroceryItemDto> items = new ArrayList<>();
		for (OrderItem item : order.getOrderItems()) {
			GenericGroceryItemDto gi=new GenericGroceryItemDto();
			GroceryItem groceryItem=item.getGroceryItem();
			gi.setName(groceryItem.getName());
			gi.setQuantity(item.getQuantity());
			gi.setPrice(item.getPrice());
			
			items.add(gi);
		}
		
		OrderDetailsDto dto=new OrderDetailsDto();
		dto.setAddress(order.getShippingAddress());
		dto.setItems(items);
		dto.setOrderId(order.getId());
		dto.setOrderDate(order.getOrderDate());
		dto.setTotalAmount(order.getTotalAmount());
		
		return dto;
	}

}
