package com.sj.ecommerce.service;

import com.sj.ecommerce.dto.InventoryDto;
import com.sj.ecommerce.dto.PageableResponse;
import com.sj.ecommerce.dto.Response;

public interface InventoryService {
    Response<InventoryDto> addInventory(InventoryDto inventoryDto);
    Response<InventoryDto> updateInventoryStock(String productId, InventoryDto inventoryDto);
    Response<String> deleteInventory(String productId);
    PageableResponse<InventoryDto> getAllInventory(int pageNumber, int pageSize, String sortBy, String sortDir);
}
