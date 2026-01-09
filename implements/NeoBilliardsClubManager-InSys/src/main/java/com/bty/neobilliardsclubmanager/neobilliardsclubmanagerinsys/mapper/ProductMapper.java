package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.mapper;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.request.ProductCreationRequest;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.request.ProductUpdateRequest;
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

    public ProductUpdateRequest toProductUpdateRequest(ProductResponse response) {
        return ProductUpdateRequest.builder()
                .id(response.getId())
                .name(response.getName())
                .price(response.getPrice())
                .description(response.getDescription())

                .build();
    }

    public Product toProduct(ProductCreationRequest request) {
        return Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .description(request.getDescription())
                .isLocked(false)
                .build();
    }
}
