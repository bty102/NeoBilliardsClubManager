package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.controller;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.response.BilliardTableResponse;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.exception.BilliardTableOpeningException;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.security.CustomUserDetails;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.service.BilliardTableService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BilliardTableController {

    final BilliardTableService billiardTableService;

    @GetMapping("/billiardTables")
    public String getBilliardTables(@RequestParam(name = "locked", required = false, defaultValue = "false") boolean isLocked,
                                    @RequestParam(name = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                                    Model model) {
        int pageSize = 6;
        Page<BilliardTableResponse> billiardTableResponses = null;
        billiardTableResponses = billiardTableService.getBilliardTablesByIsLocked(isLocked, pageNumber, pageSize);
        model.addAttribute("billiardTableResponses", billiardTableResponses);
        model.addAttribute("locked", isLocked);
        return "billiard-tables";
    }

    @GetMapping("/billiardTables/open")
    public String openBilliardTable(@RequestParam(name = "tableNumber", required = true) Long tableNumber,
                                    RedirectAttributes redirectAttributes) {

        Long currentAccountId = ((CustomUserDetails)(SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getId();

        try {
            billiardTableService.openBilliardTable(tableNumber, currentAccountId);
        } catch (BilliardTableOpeningException e) {
            redirectAttributes.addFlashAttribute("unsuccessfulTableOpeningMsg", e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        redirectAttributes.addFlashAttribute("successfulTableOpeningMsg", "Mở bàn thành công");
        return "redirect:/billiardTables";
    }
}
