package com.ecommerce.inventory.service;

import com.ecommerce.inventory.exception.InsufficientInventoryException;
import com.ecommerce.inventory.entity.Inventory;
import com.ecommerce.inventory.dto.InventoryDto;
import com.ecommerce.inventory.repository.InventoryRepository;
import com.ecommerce.inventory.response.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class InventoryService {

    private final WebClient webClient;
    private final RestTemplate restTemplate;
    private final InventoryRepository inventoryRepository;

    public InventoryService(WebClient.Builder webClientBuilder,
                            RestTemplate restTemplate,
                            InventoryRepository inventoryRepository) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8072/product-catalog-service").build();
        this.restTemplate = restTemplate;
        this.inventoryRepository = inventoryRepository;
    }

    public ProductResponse checkProductAvailability(Long productId) {
        return restTemplate.getForObject("http://localhost:8072/product-catalog-service/api/v1/products/" + productId, ProductResponse.class);
    }

    public Mono<String> checkProductAvailabilityAsync(Long productId) {
        return webClient.get()
                .uri("/api/v1/products/" + productId)
                .retrieve()
                .bodyToMono(String.class);
    }

    public void updateInventory(Long productId, Integer quantity) {
        Inventory inventory = checkInventory(productId);
        if (inventory.getQuantity() < quantity) {
            throw new InsufficientInventoryException("Insufficient inventory for productId : " + productId);
        }
        inventory.setQuantity(inventory.getQuantity() - quantity);
        inventoryRepository.save(inventory);
    }

    public Inventory checkInventory(Long productId) {
        return inventoryRepository.findByProductid(productId)
                .orElseThrow(() -> new RuntimeException("Inventory not found for product: " + productId));
    }

    public Inventory createInventory(InventoryDto inventoryDto) {
        Inventory inventory = new Inventory();
        inventory.setProductid(inventoryDto.getProductid());
        inventory.setQuantity(inventoryDto.getQuantity());
        return inventoryRepository.save(inventory);
    }
}
