package com.ecommerce.inventory.controller;

import com.ecommerce.inventory.entity.Inventory;
import com.ecommerce.inventory.dto.InventoryDto;
import com.ecommerce.inventory.service.InventoryService;
import com.ecommerce.inventory.response.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/product/availability/{productId}")
    public ResponseEntity<ProductResponse> checkProductAvailability(@PathVariable Long productId) {
        ProductResponse productResponse = inventoryService.checkProductAvailability(productId);
        return ResponseEntity.ok(productResponse);
    }

    @GetMapping("/product/availability/async/{productId}")
    public Mono<String> checkProductAvailabilityAsync(@PathVariable Long productId) {
        return inventoryService.checkProductAvailabilityAsync(productId);
    }

    @PostMapping("/addInventory")
    public ResponseEntity<Inventory> addInventory(@RequestBody InventoryDto inventoryDto) {
        return ResponseEntity.ok(inventoryService.createInventory(inventoryDto));
    }

    @PutMapping("/updateInventory/{productId}/{quantity}")
    public void updateInventory(@PathVariable Long productId, @PathVariable Integer quantity) {
        inventoryService.updateInventory(productId, quantity);
    }
}
