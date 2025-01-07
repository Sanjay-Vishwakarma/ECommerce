package com.sj.ecommerce.serviceImpl;

import com.sj.ecommerce.dto.InventoryDto;
import com.sj.ecommerce.helper.Response;
import com.sj.ecommerce.repository.InventoryRepository;
import com.sj.ecommerce.entity.Inventory;
import com.sj.ecommerce.service.InventoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Override
    public Response<InventoryDto> addInventory(InventoryDto inventoryDto) {
        // Convert DTO to entity
        Inventory inventory = modelMapper.map(inventoryDto, Inventory.class);
        
        // Save inventory to the repository
        inventoryRepository.save(inventory);
        
        // Prepare response
        Response<InventoryDto> response = new Response<>();
        response.setData(inventoryDto);
        response.setCode("200");
        response.setMessage("Inventory added successfully");
        response.setStatus("success");

        return response;
    }

    @Override
    public Response<InventoryDto> updateInventoryStock(String productId, InventoryDto inventoryDto) {
        // Fetch the existing inventory for the given product
        Optional<Inventory> inventoryOpt = inventoryRepository.findById(productId);
        
        Response<InventoryDto> response = new Response<>();

        if (inventoryOpt.isPresent()) {
            Inventory inventory = inventoryOpt.get();
            inventory.setQuantity(inventoryDto.getQuantity());
            inventory.setPrice(inventoryDto.getPrice());

            // Save the updated inventory
            inventoryRepository.save(inventory);

            // Convert updated inventory entity to DTO
            InventoryDto updatedInventoryDto = modelMapper.map(inventory, InventoryDto.class);

            response.setData(updatedInventoryDto);
            response.setCode("200");
            response.setMessage("Inventory updated successfully");
            response.setStatus("success");
        } else {
            response.setCode("404");
            response.setMessage("Inventory not found for productId: " + productId);
            response.setStatus("error");
        }

        return response;
    }

    @Override
    public Response<String> deleteInventory(String productId) {
        Response<String> response = new Response<>();

        // Check if inventory exists for the given productId
        Optional<Inventory> inventoryOpt = inventoryRepository.findById(productId);

        if (inventoryOpt.isPresent()) {
            inventoryRepository.delete(inventoryOpt.get());
            response.setCode("200");
            response.setMessage("Inventory deleted successfully");
            response.setStatus("success");
        } else {
            response.setCode("404");
            response.setMessage("Inventory not found for productId: " + productId);
            response.setStatus("error");
        }

        return response;
    }

    @Override
    public Response<List<InventoryDto>> getAllInventory() {
        Response<List<InventoryDto>> response = new Response<>();

        try {
            List<Inventory> inventoryList = inventoryRepository.findAll(); // Fetch all inventory items

            // Convert Inventory list to InventoryDto list
            List<InventoryDto> inventoryDtoList = inventoryList.stream()
                    .map(inventory -> modelMapper.map(inventory, InventoryDto.class))
                    .collect(Collectors.toList());

            response.setData(inventoryDtoList);
            response.setCode("200");
            response.setMessage("Inventory fetched successfully");
            response.setStatus("success");

        } catch (Exception e) {
            response.setData(null);
            response.setCode("500");
            response.setMessage("Error fetching inventory: " + e.getMessage());
            response.setStatus("error");
        }
        return response;
    }
}
