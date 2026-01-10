package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerexsys.controller;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerexsys.entity.Bill;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerexsys.security.CustomUserDetails;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerexsys.service.BillService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BillController {

    final BillService billService;

    @GetMapping("/bills/qr/{billId}")
    public String showQRCodeOfBill(@PathVariable(name = "billId") Long billId,
                                   Model model) {

        // https://www.vietqr.io/danh-sach-api/link-tao-ma-nhanh/
        // https://img.vietqr.io/image/<BANK_ID>-<ACCOUNT_NO>-<TEMPLATE>.png?amount=<AMOUNT>&addInfo=<DESCRIPTION>&accountName=<ACCOUNT_NAME>
        String BANK_ID = "970415";
        String ACCOUNT_NO = "113366668888";
        String TEMPLATE = "print";

        Bill bill = billService.getBillById(billId);
        if(bill.getCheckOutTime() == null) {
            model.addAttribute("msg", "Hóa đơn đang được xử lý");
            return "qrcode-of-bill";
        }
        Long amount = bill.getTotalAmount();
        String addInfo = "Chuyển tiền bắn bida - HD " + bill.getId();

        String QR_URL = String.format("https://img.vietqr.io/image/%s-%s-%s.png?amount=%s&addInfo=%s", BANK_ID, ACCOUNT_NO, TEMPLATE, amount.toString(), addInfo);
        model.addAttribute("QR_URL", QR_URL);
        return "qrcode-of-bill";
    }

    @GetMapping("bills/current")
    public String getPlayingInfo(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        List<Bill> bills = null;
        try {
            bills = billService.getCurrentPlayingBill(userDetails.getMember().getId());
        } catch (Exception e) {

        }
        model.addAttribute("bills", bills);
        return "playing-info";
    }
}
