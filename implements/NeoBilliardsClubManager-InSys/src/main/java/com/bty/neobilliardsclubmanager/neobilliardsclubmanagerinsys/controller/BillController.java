package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.controller;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.response.BillResponse;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.response.BilliardTableResponse;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.exception.BilliardTableNotFoundException;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.service.BillService;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.service.BilliardTableService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BillController {

    final BillService billService;
    final BilliardTableService billiardTableService;

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
}
