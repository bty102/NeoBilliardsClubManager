package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.controller;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.request.BilliardTableCreationRequest;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.request.BilliardTableUpdateRequest;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.response.BilliardTableResponse;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.response.BilliardTableTypeResponse;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.entity.BilliardTable;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.exception.BilliardTableClosingException;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.exception.BilliardTableOpeningException;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.infrastructure.FileUtil;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.mapper.BilliardTableMapper;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.security.CustomUserDetails;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.service.BilliardTableService;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.service.BilliardTableTypeService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BilliardTableController {

    final BilliardTableService billiardTableService;
    final FileUtil fileUtil;
    final BilliardTableTypeService billiardTableTypeService;
    final BilliardTableMapper billiardTableMapper;

    @Value("${image.billiard-table.dir}")
    String BILLIARD_TABLE_IMAGE_DIR;

    @Value("${server.port}")
    String SERVER_PORT;

    @Value("${server.servlet.context-path}")
    String SERVER_CONTEXT_PATH;

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

    @GetMapping("/billiardTables/close")
    public String closeBilliardTable(@RequestParam(name = "tableNumber", required = true) Long tableNumber,
                                     RedirectAttributes redirectAttributes) {

        try {
            billiardTableService.closeBilliardTable(tableNumber);
        } catch (BilliardTableClosingException e) {
            redirectAttributes.addFlashAttribute("unsuccessfulTableClosingMsg", "Đóng bàn thất bại");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        redirectAttributes.addFlashAttribute("successfulTableClosingMsg", "Đóng bàn thành công");
        return "redirect:/billiardTables";
    }

    @GetMapping("/billiardTables/uploadImage")
    public String showUploadImageForBilliardTable(@RequestParam(name = "tableNumber", required = true) Long tableNumber,
                                                  Model model) {
        BilliardTableResponse billiardTableResponse = billiardTableService.getBilliardTableByTableNumber(tableNumber);
        model.addAttribute("billiardTableResponse", billiardTableResponse);
        return "upload-image-for-billiard-table";
    }

    @PostMapping("/billiardTables/uploadImage")
    public String uploadImageForBilliardTable(@RequestParam(name = "imageFile", required = true) MultipartFile imageFile,
                                              @RequestParam(name = "tableNumber", required = true) Long tableNumber,
                                              RedirectAttributes redirectAttributes) {

        boolean imageIsSaved = false;
        Map<String, String> savedData = null;
        try {
            savedData = fileUtil.saveImageFile(imageFile, BILLIARD_TABLE_IMAGE_DIR);
            imageIsSaved = true;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("unsuccessfulBilliardTableImageSavingMsg", e.getMessage());
        }

        if(imageIsSaved) {

            String imageURL = "http://localhost:"
                    + SERVER_PORT

                    + SERVER_CONTEXT_PATH
                    + "/billiard-table-image/"
                    + savedData.get("fileName");
            billiardTableService.updateBilliardTableImagePath(tableNumber, imageURL);
        }

        return "redirect:/billiardTables";
    }

    @GetMapping("/billiardTables/update/{tableNumber}")
    public String showUpdateBilliardTable(@PathVariable(name = "tableNumber") Long tableNumber,
                                          Model model) {

        BilliardTableResponse billiardTableResponse = billiardTableService.getBilliardTableByTableNumber(tableNumber);
        BilliardTableUpdateRequest billiardTableUpdateRequest = billiardTableMapper.toBilliardTableUpdateRequest(billiardTableResponse);
        model.addAttribute("billiardTableUpdateRequest", billiardTableUpdateRequest);

        List<BilliardTableTypeResponse> billiardTableTypeResponses = billiardTableTypeService.getAll();
        model.addAttribute("billiardTableTypeResponses", billiardTableTypeResponses);

        return "billiard-table-update-form";
    }

    @PostMapping("/billiardTables/do-update")
    public String updateBilliardTable(@Valid @ModelAttribute("billiardTableUpdateRequest") BilliardTableUpdateRequest request,
                                      BindingResult bindingResult,
                                      Model model) {
        if(bindingResult.hasErrors()) {

            List<BilliardTableTypeResponse> billiardTableTypeResponses = billiardTableTypeService.getAll();
            model.addAttribute("billiardTableTypeResponses", billiardTableTypeResponses);
            return "billiard-table-update-form";
        }

        billiardTableService.updateBilliardTable(request);
        return "redirect:/billiardTables";
    }

    @GetMapping("/billiardTables/create")
    public String showCreateBilliardTable(Model model) {
        model.addAttribute("billiardTableCreationRequest", new BilliardTableCreationRequest());

        List<BilliardTableTypeResponse> billiardTableTypeResponses = billiardTableTypeService.getAll();
        model.addAttribute("billiardTableTypeResponses", billiardTableTypeResponses);
        return "billiard-table-creation-form";
    }

    @PostMapping("/billiardTables/do-create")
    public String createBilliardTable(@Valid @ModelAttribute("billiardTableCreationRequest") BilliardTableCreationRequest request,
                                      BindingResult bindingResult,
                                      Model model) {
        if(bindingResult.hasErrors()) {
            List<BilliardTableTypeResponse> billiardTableTypeResponses = billiardTableTypeService.getAll();
            model.addAttribute("billiardTableTypeResponses", billiardTableTypeResponses);
            return "billiard-table-creation-form";
        }

        billiardTableService.createBilliardTable(request);
        return "redirect:/billiardTables";
    }

    @GetMapping("/billiardTables/lock/{tableNumber}")
    public String lockBilliardTable(@PathVariable(name = "tableNumber") Long tableNumber) {

        billiardTableService.lockBilliardTable(tableNumber);

        return "redirect:/billiardTables?locked=true";
    }

    @GetMapping("/billiardTables/unlock/{tableNumber}")
    public String unlockBilliardTable(@PathVariable(name = "tableNumber") Long tableNumber) {

        billiardTableService.unlockBilliardTable(tableNumber);

        return "redirect:/billiardTables?locked=false";
    }
}
