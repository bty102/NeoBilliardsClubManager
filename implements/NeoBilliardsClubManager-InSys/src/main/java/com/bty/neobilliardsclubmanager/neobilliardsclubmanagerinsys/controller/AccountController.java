package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.controller;

import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.request.EmployeeProfileCreationRequest;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.response.AccountResponse;
import com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.service.AccountService;
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
public class AccountController {

    final AccountService accountService;

    @GetMapping("/accounts/employees")
    public String getEmployees(@RequestParam(name = "key", required = false, defaultValue = "") String key,
                                Model model) {
        List<AccountResponse> employees = null;
        if(key.isEmpty()) {
            employees = accountService.getAllEmployees();
        } else {
            employees = accountService.findEmployeesByFullNameContainingOrAddressContaining(key);
        }

        model.addAttribute("employees", employees);
        return "employees";
    }

    @GetMapping("/accounts/employees/create")
    public String showCreateEmployee(Model model) {
        model.addAttribute("employeeProfileCreationRequest", new EmployeeProfileCreationRequest());
        return "employee-creation-form";
    }

    @PostMapping("/accounts/employees/do-create")
    public String createEmployee(@Valid @ModelAttribute("employeeProfileCreationRequest") EmployeeProfileCreationRequest request,
                                 BindingResult bindingResult,
                                 @RequestParam(name = "username") String username,
                                 @RequestParam(name = "password") String password,
                                 Model model) {
       if (bindingResult.hasErrors()) {

           return "employee-creation-form";
       }
       accountService.createEmployeeAccount(username, password, request);
       return "redirect:/accounts/employees";
    }

    @GetMapping("/accounts/employees/lock/{accountId}")
    public String lockEmployeeAccount(@PathVariable(name = "accountId") Long accountId) {

        accountService.lockEmployeeAccount(accountId);
        return "redirect:/accounts/employees";
    }

    @GetMapping("/accounts/employees/unlock/{accountId}")
    public String unlockEmployeeAccount(@PathVariable(name = "accountId") Long accountId) {

        accountService.unlockEmployeeAccount(accountId);
        return "redirect:/accounts/employees";
    }
}
