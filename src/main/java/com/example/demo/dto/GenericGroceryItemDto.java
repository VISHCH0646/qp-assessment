package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@JsonInclude(value = Include.NON_NULL)
@Data
public class GenericGroceryItemDto {
	
	private Long id;

	@NotBlank(message = "Item Name should not be blank")
	private String name;
	
	@NotNull(message = "Item Price should not be blank")
	@Min(value = 1)
	private Double price;
	
	@NotNull(message = "Item Quantity should not be blank")
	@Min(value = 1)
	private Integer quantity;
	
	@NotBlank(message = "Item description should not be blank")
	private String description;
}
