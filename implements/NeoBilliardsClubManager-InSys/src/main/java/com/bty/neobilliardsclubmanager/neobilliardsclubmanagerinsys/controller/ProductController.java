package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.controller;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.request.ProductCreationRequest;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.request.ProductUpdateRequest;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.response.ProductResponse;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.mapper.ProductMapper;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.service.ProductService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductController {

    final ProductService productService;
    final ProductMapper productMapper;

    @GetMapping("/products")
    public String getProducts(@RequestParam(name = "key", required = false, defaultValue = "") String key,
                              Model model) {
        List<ProductResponse> productResponses = null;
        if(key.isEmpty()) {

            productResponses = productService.getAllProducts();
        } else {
            productResponses = productService.findProductsByNameContaining(key);
        }

        model.addAttribute("productResponses", productResponses);
        return "products";
    }

    @GetMapping("/products/update/{id}")
    public String showUpdateProduct(@PathVariable(name = "id") Long id,
                                    Model model) {
        ProductResponse productResponse = productService.getProductById(id);
        ProductUpdateRequest productUpdateRequest = productMapper.toProductUpdateRequest(productResponse);
        model.addAttribute("productUpdateRequest", productUpdateRequest);
        return "product-update-form";
    }

    @PostMapping("/products/do-update")
    public String updateProduct(@Valid @ModelAttribute("productUpdateRequest") ProductUpdateRequest request,
                                BindingResult bindingResult,
                                Model model) {
        if(bindingResult.hasErrors()) {

            return "product-update-form";
        }
        productService.updateProduct(request);
        return "redirect:/products";
    }

    @GetMapping("/products/create")
    public String showCreateProduct(Model model) {
        model.addAttribute("productCreationRequest", new ProductCreationRequest());
        return "product-creation-form";
    }

    @PostMapping("/products/do-create")
    public String createProduct(@Valid @ModelAttribute("productCreationRequest") ProductCreationRequest request,
                                BindingResult bindingResult,
                                Model model) {
        if(bindingResult.hasErrors()) {
            return "product-creation-form";
        }
        productService.createProduct(request);
        return "redirect:/products";
    }
}
