package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.GroceryItem;

public interface GroceryItemRepo extends JpaRepository<GroceryItem, Long> {

	@Query("select g from GroceryItem g where g.id in (:itemIds)")
	List<GroceryItem> getByIds(@Param(value = "itemIds") List<Long> itemIds);
	
}
