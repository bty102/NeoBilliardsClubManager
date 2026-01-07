package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.controller;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.response.BillResponse;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.response.BilliardTableResponse;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.response.MemberResponse;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.exception.BilliardTableNotFoundException;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.service.BillService;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.service.BilliardTableService;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.service.MemberService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BillController {

    final BillService billService;
    final BilliardTableService billiardTableService;
    final MemberService memberService;

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
}
