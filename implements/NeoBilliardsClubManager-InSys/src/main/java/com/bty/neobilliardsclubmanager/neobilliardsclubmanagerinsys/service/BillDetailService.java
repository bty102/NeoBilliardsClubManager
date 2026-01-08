package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.service;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.request.BillDetailCreationRequest;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.request.BillDetailUpdateRequest;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.response.BillDetailResponse;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.Bill;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.BillDetail;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.Product;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.exception.BillDetailCreationException;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.exception.BillDetailDeletionException;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.exception.BillDetailNotFoundException;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.exception.BillDetailUpdateException;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.mapper.BillDetailMapper;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.repository.BillDetailRepository;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.repository.BillRepository;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.repository.ProductRepository;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Validated
public class BillDetailService {

    final BillDetailRepository billDetailRepository;
    final BillDetailMapper billDetailMapper;
    final BillRepository billRepository;
    final ProductRepository productRepository;
    final BillService billService;

    public List<BillDetailResponse> getAllBillDetailsByBillId(Long billId) {
        return billDetailRepository.findByBill_Id(billId).
                stream()
                .map(billDetail -> billDetailMapper.toBillDetailResponse(billDetail))
                .toList();
    }

    @PreAuthorize("hasRole(T(com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.constant.Role).ADMIN.name()) or @billService.isOwnerOfBill(#request.billId, authentication.principal.id)")
    @Transactional(rollbackFor = Exception.class)
    public void createBillDetail(@Valid BillDetailCreationRequest request) {
        Bill bill = billRepository.findById(request.getBillId())
                .orElseThrow(() -> {throw new BillDetailCreationException("Không tìm thấy hóa đơn");
                });
        if(bill.getPaid()) {
            throw new BillDetailCreationException("Không thể tạo chi tiết hóa đơn");
        }

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> {throw new BillDetailCreationException("Không tìm thấy mặt hàng");});
        if(product.getIsLocked()) {
            throw new BillDetailCreationException("Không thể tạo chi tiết hóa đơn");
        }

//        billDetailRepository.findByBillAndProduct(bill, product)
//                .orElseThrow(() -> {throw new BillDetailCreationException("Đã tồn tại mặt hàng trong hóa đơn");});
        if(billDetailRepository.existsByBillAndProduct(bill, product)) {
            throw new BillDetailCreationException("Đã tồn tại mặt hàng trong hóa đơn");
        }

        BillDetail billDetail = billDetailMapper.toBillDetail(request);
        billDetailRepository.save(billDetail);

        if(bill.getCheckOutTime() != null) {
            billService.calculateAndUpdateTotalAmount(bill.getId());
        }
    }

    @PreAuthorize("hasRole(T(com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.constant.Role).ADMIN.name()) or @billDetailService.isOwnerOfBillDetail(#request.id, authentication.principal.id)")
    @Transactional(rollbackFor = Exception.class)
    public void updateBillDetail(@Valid BillDetailUpdateRequest request) {
        BillDetail billDetail= billDetailRepository.findById(request.getId())
                .orElseThrow(() -> {throw new BillDetailUpdateException("Không tồn tại chi tiết hóa đơn");
                });
        if(billDetail.getBill().getPaid()) {
            throw new BillDetailUpdateException("Không thể cập nhật chi tiết hóa đơn");
        }

        billDetail.setQuantity(request.getQuantity());
        billDetailRepository.save(billDetail);

        if(billDetail.getBill().getCheckOutTime() != null) {
            billService.calculateAndUpdateTotalAmount(billDetail.getBill().getId());
        }
    }

    public boolean isOwnerOfBillDetail(Long billDetailId, Long AccountId) {
        Optional<BillDetail> billDetail = billDetailRepository.findById(billDetailId);
        if(!billDetail.isPresent()) {
            return false;
        }
        if(billDetail.get().getBill().getAccount().getId().equals(AccountId)) {
            return true;
        }
        return false;
    }

//    @PreAuthorize("hasRole(T(com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.constant.Role).ADMIN.name()) or @billDetailService.isOwnerOfBillDetail(#request.id, authentication.principal.id)")
//    @Transactional(rollbackFor = Exception.class)
//    public void deleteBillDetailById(Long id) {
//        BillDetail billDetail = billDetailRepository.findById(id)
//                .orElseThrow(() -> {throw new BillDetailNotFoundException("Không tìm thấy chi tiết hóa đơn");
//                });
//        if(billDetail.getBill().getPaid()) {
//            throw new BillDetailDeletionException("Hóa đơn đã thanh toán, khôn thể xóa chi tiết hóa đơn");
//        }
//
//        billDetailRepository.deleteById(id);
//
//        if(billDetail.getBill().getCheckOutTime() != null) {
//            billService.calculateAndUpdateTotalAmount(billDetail.getBill().getId());
//        }
//    }

    public BillDetailResponse getBillDetailById(Long id) {
        BillDetail billDetail = billDetailRepository.findById(id)
                .orElseThrow(() -> {throw new BillDetailNotFoundException("Không tìm thấy chi tiết hóa đơn");});
        return billDetailMapper.toBillDetailResponse(billDetail);
    }
}
