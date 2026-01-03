package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.controller;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.service.BillService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BillController {

    final BillService billService;

    @GetMapping("/bills")
    public String getBills(@RequestParam(name = "tableNumber", required = false) Long tableNumber) {

        return "";
    }
}
