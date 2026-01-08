package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.service;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.response.ProductResponse;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.mapper.ProductMapper;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductService {

    final ProductRepository productRepository;
    final ProductMapper productMapper;

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(product -> productMapper.toProductResponse(product))
                .toList();
    }

    public List<ProductResponse> findProductsByIsLocked(boolean isLocked) {
        return productRepository.findByIsLocked(isLocked)
                .stream()
                .map(product -> productMapper.toProductResponse(product)).toList();
    }
}
