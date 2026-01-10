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

import java.text.Bidi;
import java.util.List;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HistoryController {

    final BillService billService;

    @GetMapping("/history")
    public String showHistory(@AuthenticationPrincipal CustomUserDetails userDetails,
                              Model model) {
        List<Bill> bills = billService.getBillsByMemberId(userDetails.getMember().getId());
        model.addAttribute("bills", bills);
        return "playing-history";
    }
}
