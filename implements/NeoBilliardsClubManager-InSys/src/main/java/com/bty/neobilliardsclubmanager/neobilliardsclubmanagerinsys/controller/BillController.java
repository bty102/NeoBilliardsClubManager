package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.controller;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.request.BillDetailCreationRequest;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.request.BillDetailUpdateRequest;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.response.*;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.exception.BillDetailCreationException;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.exception.BillDetailUpdateException;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.exception.BillPaymentConfirmationException;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.exception.BilliardTableNotFoundException;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.service.*;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BillController {

    final BillService billService;
    final BilliardTableService billiardTableService;
    final MemberService memberService;
    final BillDetailService billDetailService;
    final ProductService productService;

    @GetMapping("/bills")
    public String getBills(@RequestParam(name = "tableNumber", required = false) Long tableNumber,
                           @RequestParam(name = "pageNumber", required = false, defaultValue = "1") int pageNumber, Model model) {

        int pageSize = 5;
        Page<BillResponse> billResponses = null;
        if(tableNumber != null) {
            billResponses = billService.findBillsByTableNumber(tableNumber, pageNumber, pageSize);
            try {
                BilliardTableResponse billiardTableResponse = billiardTableService.getBilliardTableByTableNumber(tableNumber);
                model.addAttribute("billiardTableResponse", billiardTableResponse);
            } catch (BilliardTableNotFoundException e) {
                model.addAttribute("billiardTableNotFoundMsg", e.getMessage());
            }
            model.addAttribute("tableNumber", tableNumber);
        } else {
            billResponses = billService.getBills(pageNumber, pageSize);
        }
        model.addAttribute("billResponses", billResponses);


        return "bills";
    }

    @GetMapping("bills/updateMember")
    public String showUpdateMemberOfBill(@RequestParam(name = "billId", required = true) Long billId,
                                         @RequestParam(name = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                                         Model model) {

        BillResponse billResponse = billService.getBillById(billId);
        model.addAttribute("billResponse", billResponse);

        int pageSize = 5;
        Page<MemberResponse> memberResponses = memberService.getMembers(pageNumber, pageSize);
        model.addAttribute("memberResponses", memberResponses);

        return "update-member-of-bill";
    }

    @GetMapping("bills/doUpdateMember")
    public String updateMemberOfBill(@RequestParam(name = "billId", required = true) Long billId,
                                     @RequestParam(name = "memberId", required = true) Long memberId,
                                     RedirectAttributes redirectAttributes) {

        billService.updateMemberOfBill(billId, memberId);
//        redirectAttributes.addFlashAttribute("");
        return "redirect:/bills/updateMember?billId=" + billId;
    }

    @GetMapping("bills/updateProducts")
    public String showUpdateProductsOfBill(@RequestParam(name = "billId", required = true) Long billId,
                                           Model model) {

        BillResponse billResponse = billService.getBillById(billId);
        model.addAttribute("billResponse", billResponse);

        List<BillDetailResponse> billDetailResponses = billDetailService.getAllBillDetailsByBillId(billId);
        model.addAttribute("billDetailReponses", billDetailResponses);

        List<ProductResponse> productResponses = productService.findProductsByIsLocked(false);
        model.addAttribute("productResponses", productResponses);

        return "update-products-of-bill";
    }

    @PostMapping("bills/updateProducts/add")
    public String addProductForBill(@Valid @ModelAttribute("billDetailCreationRequest") BillDetailCreationRequest request,
                                    BindingResult bindingResult,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()) {

            BillResponse billResponse = billService.getBillById(request.getBillId());
            model.addAttribute("billResponse", billResponse);

            List<BillDetailResponse> billDetailResponses = billDetailService.getAllBillDetailsByBillId(request.getBillId());
            model.addAttribute("billDetailReponses", billDetailResponses);

            List<ProductResponse> productResponses = productService.findProductsByIsLocked(false);
            model.addAttribute("productResponses", productResponses);

            return "update-products-of-bill";
        }

        try {
            billDetailService.createBillDetail(request);
        } catch (BillDetailCreationException e) {
            redirectAttributes.addFlashAttribute("unsuccessfulBillDetailCreationMsg", e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return "redirect:/bills/updateProducts?billId=" + request.getBillId();
    }

    @PostMapping("bills/updateProducts/edit")
    public String editProductForBill(@Valid @ModelAttribute("billDetailUpdateRequest") BillDetailUpdateRequest request,
                                     BindingResult bindingResult,
                                     Model model,
                                     RedirectAttributes redirectAttributes,
                                     @RequestParam(name = "billId") Long billId) {

        if(bindingResult.hasErrors()) {

            BillResponse billResponse = billService.getBillById(billId);
            model.addAttribute("billResponse", billResponse);

            List<BillDetailResponse> billDetailResponses = billDetailService.getAllBillDetailsByBillId(billId);
            model.addAttribute("billDetailReponses", billDetailResponses);

            List<ProductResponse> productResponses = productService.findProductsByIsLocked(false);
            model.addAttribute("productResponses", productResponses);

            return "update-products-of-bill";
        }

        try {
            billDetailService.updateBillDetail(request);
        } catch (BillDetailUpdateException e) {
            redirectAttributes.addFlashAttribute("unsuccessfulBillDetailUpdateMsg", e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "redirect:/bills/updateProducts?billId=" + billId;
    }

//    @GetMapping("bills/updateProducts/delete")
//    public String deleteProductOfBill(@RequestParam(name = "billDetailId", required = true) Long billDetailId
//                                      ) {
//
//        BillDetailResponse billDetailResponse = billDetailService.getBillDetailById(billDetailId);
//
//        billDetailService.deleteBillDetailById(billDetailId);
//
//        return "redirect:/bills/updateProducts?billId=" + billDetailResponse.getBill().getId();
//    }

    @GetMapping("/bills/qr/{billId}")
    public String showQRCodeOfBill(@PathVariable(name = "billId") Long billId,
                                   Model model) {

        // https://www.vietqr.io/danh-sach-api/link-tao-ma-nhanh/
        // https://img.vietqr.io/image/<BANK_ID>-<ACCOUNT_NO>-<TEMPLATE>.png?amount=<AMOUNT>&addInfo=<DESCRIPTION>&accountName=<ACCOUNT_NAME>
        String BANK_ID = "970415";
        String ACCOUNT_NO = "113366668888";
        String TEMPLATE = "print";

        BillResponse billResponse = billService.getBillById(billId);
        if(billResponse.getCheckOutTime() == null) {
            model.addAttribute("msg", "Hóa đơn đang được xử lý");
            return "qrcode-of-bill";
        }
        Long amount = billResponse.getTotalAmount();
        String addInfo = "Chuyển tiền bắn bida - HD " + billResponse.getId();

        String QR_URL = String.format("https://img.vietqr.io/image/%s-%s-%s.png?amount=%s&addInfo=%s", BANK_ID, ACCOUNT_NO, TEMPLATE, amount.toString(), addInfo);
        model.addAttribute("QR_URL", QR_URL);
        return "qrcode-of-bill";
    }

    @GetMapping("/bills/confirmPayment/{billId}")
    public String confirmPaymentForBill(@PathVariable(name = "billId") Long billId,
                                        RedirectAttributes redirectAttributes) {

        try {
            billService.confirmPayment(billId);
            redirectAttributes.addFlashAttribute("successfulBillPaymentConfirmationMsg", "Xác nhận thành công");
        } catch (BillPaymentConfirmationException e) {
            redirectAttributes.addFlashAttribute("unsuccessfulBillPaymentConfirmationMsg", e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "redirect:/bills";
    }
}
