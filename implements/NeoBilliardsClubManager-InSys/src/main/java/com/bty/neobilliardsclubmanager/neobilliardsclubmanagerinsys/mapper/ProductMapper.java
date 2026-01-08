package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.mapper;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.response.ProductResponse;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.Product;
import org.springframework.stereotype.Component;

@Component

public class ProductMapper {

    public ProductResponse toProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .isLocked(product.getIsLocked())
                .build();
    }
}
