package com.ecommerce.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InventoryDto {

    private Long id;
    private Long productid;
    private Integer quantity;
}
