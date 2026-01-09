package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.controller;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.request.EmployeeProfileUpdateRequest;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.response.EmployeeProfileResponse;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.mapper.EmployeeProfileMapper;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.service.EmployeeProfileService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeProfileController {

    final EmployeeProfileService employeeProfileService;
    final EmployeeProfileMapper employeeProfileMapper;

    @GetMapping("employeeProfiles/update/{profileId}")
    public String showUpdateEmployeeProfile(@PathVariable(name = "profileId") Long profileId,
                                            Model model) {
        EmployeeProfileResponse employeeProfileResponse = employeeProfileService.getEmployeeProfileById(profileId);
        EmployeeProfileUpdateRequest employeeProfileUpdateRequest = employeeProfileMapper.toEmployeeProfileUpdateRequest(employeeProfileResponse);
        model.addAttribute("employeeProfileUpdateRequest", employeeProfileUpdateRequest);
        return "employee-profile-update-form";
    }

    @PostMapping("employeeProfiles/do-update")
    public String updateEmployeeProfile(@Valid @ModelAttribute("employeeProfileUpdateRequest") EmployeeProfileUpdateRequest request,
                                        BindingResult bindingResult,
                                        Model model) {
        if (bindingResult.hasErrors()) {
            return "employee-profile-update-form";
        }
        employeeProfileService.updateEmployeeProfile(request);
        return "redirect:/accounts/employees";
    }
}
