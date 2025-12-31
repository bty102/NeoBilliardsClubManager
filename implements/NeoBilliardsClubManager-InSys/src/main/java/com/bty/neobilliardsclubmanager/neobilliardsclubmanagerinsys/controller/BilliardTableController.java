package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BilliardTableController {

    @GetMapping("/billiardTables")
    public String getBilliardTables() {

        return "billiard-tables";
    }
}
