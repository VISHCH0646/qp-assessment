package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(value = Include.NON_NULL)
public class OrderDetailsDto {
	
	private Long orderId;
	
	private String address;
	
	private List<GenericGroceryItemDto> items;
	
	private Double totalAmount;
	
	private LocalDateTime orderDate;

}
