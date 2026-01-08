package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.mapper;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.request.BillDetailCreationRequest;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.response.BillDetailResponse;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.Bill;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.BillDetail;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.Product;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.exception.BillNotFoundException;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.exception.ProductNotFoundException;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.repository.BillRepository;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BillDetailMapper {

    final BillMapper billMapper;
    final ProductMapper productMapper;
    final BillRepository billRepository;
    final ProductRepository productRepository;

    public BillDetailResponse toBillDetailResponse(BillDetail billDetail) {
        return BillDetailResponse.builder()
                .id(billDetail.getId())
                .quantity(billDetail.getQuantity())
                .bill(billMapper.toBillResponse(billDetail.getBill()))
                .product(productMapper.toProductResponse(billDetail.getProduct()))
                .build();
    }

    public BillDetail toBillDetail(BillDetailCreationRequest request) {
        Bill bill = billRepository.findById(request.getBillId())
                .orElseThrow(() -> {throw new BillNotFoundException("Không tìm thấy hóa đơn");
                });
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> {throw new ProductNotFoundException("Không tìm thấy mặt hàng");
                });

        return BillDetail.builder()
                .quantity(request.getQuantity())
                .bill(bill)
                .product(product)
                .build();
    }
}
