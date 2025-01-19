package com.sj.ecommerce.controller;

import com.sj.ecommerce.dto.InventoryDto;
import com.sj.ecommerce.dto.PageableResponse;
import com.sj.ecommerce.dto.Response;
import com.sj.ecommerce.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/inventory")
public class InventoryController {


    @Autowired
    private InventoryService inventoryService;


    // Add inventory
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/addProduct")
    public Response<InventoryDto> addInventory(@RequestBody InventoryDto inventoryDto) {
        return inventoryService.addInventory(inventoryDto);
    }

    // Update inventory stock
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{productId}")
    public Response<InventoryDto> updateInventoryStock(
            @PathVariable String productId, @RequestBody InventoryDto inventoryDto) {
        return inventoryService.updateInventoryStock(productId, inventoryDto);
    }

    // Delete inventory
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{productId}")
    public Response<String> deleteInventory(@PathVariable String productId) {
        return inventoryService.deleteInventory(productId);
    }

    // get all inventory
    @GetMapping("/getAllInventory")
    public PageableResponse<InventoryDto> getAllInventory(
            @RequestParam(value = "pageNumber", defaultValue = "1", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "name", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        return inventoryService.getAllInventory(pageNumber, pageSize, sortBy, sortDir);
    }
}
