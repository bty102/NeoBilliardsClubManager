package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.controller;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.request.BilliardTableTypeCreationRequest;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.request.BilliardTableTypeUpdateRequest;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.response.BilliardTableTypeResponse;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.mapper.BilliardTableTypeMapper;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.repository.BilliardTableTypeRepository;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.service.BilliardTableTypeService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BilliardTableTypeController {

    final BilliardTableTypeService billiardTableTypeService;
    final BilliardTableTypeMapper billiardTableTypeMapper;

    @GetMapping("/billiardTableTypes")
    public String getBilliardTableTypes(@RequestParam(name = "key", required = false, defaultValue = "") String key,
                                        Model model) {

        List<BilliardTableTypeResponse> billiardTableTypeResponses = null;
        if(key.isEmpty()) {
            billiardTableTypeResponses = billiardTableTypeService.getAll();
        } else {
            billiardTableTypeResponses = billiardTableTypeService.findBilliardTableTypesByNameContaining(key);
        }
        model.addAttribute("billiardTableTypeResponses", billiardTableTypeResponses);

        return "billiard-table-types";
    }

    @GetMapping("/billiardTableTypes/update/{id}")
    public String showUpdateBilliardTableType(@PathVariable(name = "id") Long id, Model model) {
        BilliardTableTypeResponse billiardTableTypeResponse = billiardTableTypeService.getBilliardTableTypeById(id);
        BilliardTableTypeUpdateRequest billiardTableTypeUpdateRequest = billiardTableTypeMapper.toBilliardTableTypeUpdateRequest(billiardTableTypeResponse);
        model.addAttribute("billiardTableTypeUpdateRequest", billiardTableTypeUpdateRequest);

        return "billiard-table-type-update-form";
    }

    @PostMapping("/billiardTableTypes/do-update")
    public String updateBilliardTableType(@Valid @ModelAttribute("billiardTableTypeUpdateRequest") BilliardTableTypeUpdateRequest request,
                                          BindingResult bindingResult,
                                          Model model) {
        if(bindingResult.hasErrors()) {

            return "billiard-table-type-update-form";
        }
        billiardTableTypeService.updateBilliardTableType(request);

        return "redirect:/billiardTableTypes";
    }

    @GetMapping("/billiardTableTypes/create")
    public String showCreateBilliardTableType(Model model) {
        model.addAttribute("billiardTableTypeCreationRequest", new BilliardTableTypeCreationRequest());
        return "billiard-table-type-creation-form";
    }

    @PostMapping("/billiardTableTypes/do-create")
    public String createBilliardTableType(@Valid @ModelAttribute("billiardTableTypeCreationRequest") BilliardTableTypeCreationRequest request,
                                          BindingResult bindingResult,
                                          Model model) {
       if(bindingResult.hasErrors()) {

           return "billiard-table-type-creation-form";
       }

       billiardTableTypeService.createBilliardTableType(request);
       return "redirect:/billiardTableTypes";
    }
}
