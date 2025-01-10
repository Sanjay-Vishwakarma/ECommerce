package com.sj.ecommerce.serviceImpl;

import com.sj.ecommerce.dto.InventoryDto;
import com.sj.ecommerce.dto.PageableResponse;
import com.sj.ecommerce.helper.PageHelper;
import com.sj.ecommerce.dto.Response;
import com.sj.ecommerce.repository.InventoryRepository;
import com.sj.ecommerce.entity.Inventory;
import com.sj.ecommerce.service.InventoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private InventoryRepository inventoryRepository;

    private final PageHelper pageHelper;

    @Autowired
    public InventoryServiceImpl(PageHelper pageHelper) {
        this.pageHelper = pageHelper;
    }

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
    public PageableResponse<InventoryDto> getAllInventory(int pageNumber, int pageSize, String sortBy, String sortDir) {
        try {
            // Determine sorting direction
            Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

            // Create Pageable object (adjust page number to zero-based indexing)
            Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

            // Fetch paginated inventory items
            Page<Inventory> inventoryPage = inventoryRepository.findAll(pageable);

            // Use Helper to convert Page<Inventory> to PageableResponse<InventoryDto>
            return pageHelper.getPageableResponse(inventoryPage, InventoryDto.class);

        } catch (Exception e) {
            throw new RuntimeException("Error fetching inventory: " + e.getMessage(), e); // Handle errors appropriately
        }
    }

}
