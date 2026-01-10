package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.service;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.request.ProductCreationRequest;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.request.ProductUpdateRequest;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.response.ProductResponse;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.Product;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.exception.ProductCreationException;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.exception.ProductNotFoundException;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.exception.ProductUpdateException;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.mapper.ProductMapper;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.repository.ProductRepository;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Validated
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

    public List<ProductResponse> findProductsByNameContaining(String key) {
        List<Product> products = productRepository.findByNameContaining(key);
        return products
                .stream()
                .map(product -> productMapper.toProductResponse(product))
                .toList();
    }

    @PreAuthorize("hasRole(T(com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.constant.Role).ADMIN.name())")
    public void updateProduct(@Valid ProductUpdateRequest request) {
        Product product = productRepository.findById(request.getId())
                .orElseThrow(() -> {throw new ProductNotFoundException("Không tìm thấy sản phẩm");
                });

        if(!product.getName().equals(request.getName())
        && productRepository.existsByName(request.getName())) {
            throw new ProductUpdateException("Tên mặt hàng đã tồn tại");
        }

        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        productRepository.save(product);
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {throw new ProductNotFoundException("Không tìm thấy mặt hàng");});
        return productMapper.toProductResponse(product);
    }

    @PreAuthorize("hasRole(T(com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.constant.Role).ADMIN.name())")
    public void createProduct(@Valid ProductCreationRequest request) {
        if(productRepository.existsByName(request.getName())) {
            throw new ProductCreationException("Tên sản phẩm đã tồn tại");
        }
        Product product = productMapper.toProduct(request);
        productRepository.save(product);
    }

    @PreAuthorize("hasRole(T(com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.constant.Role).ADMIN.name())")
    public void lockProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {throw new ProductNotFoundException("Không tìm thấy mặt hàng");});
        product.setIsLocked(true);
        productRepository.save(product);
    }

    @PreAuthorize("hasRole(T(com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.constant.Role).ADMIN.name())")
    public void unlockProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {throw new ProductNotFoundException("Không tìm thấy mặt hàng");});
        product.setIsLocked(false);
        productRepository.save(product);
    }
}
